package com.tele.tuitionbot.service;

import com.tele.tuitionbot.api.TelegramApi;
import com.tele.tuitionbot.entity.Member;
import com.tele.tuitionbot.entity.PollQuestion;
import com.tele.tuitionbot.entity.PollQuestionOption;
import com.tele.tuitionbot.enums.AdminCommands;
import com.tele.tuitionbot.repository.MemberRepository;
import com.tele.tuitionbot.repository.PollQuestionOptionRepository;
import com.tele.tuitionbot.repository.PollQuestionRepository;
import com.tele.tuitionbot.request.PollRequestDto;
import com.tele.tuitionbot.util.Constants;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

import java.util.ArrayList;
import java.util.List;

public class AdminService {

    private final SilentSender sender;

    private final MemberRepository memberRepository;

    private final PollQuestionRepository pollQuestionRepository;

    private final PollQuestionOptionRepository pollQuestionOptionRepository;

    private final TelegramApi telegramApi;

    public AdminService(SilentSender sender, MemberRepository memberRepository, PollQuestionRepository pollQuestionRepository, PollQuestionOptionRepository pollQuestionOptionRepository, TelegramApi telegramApi) {
        this.sender = sender;
        this.memberRepository = memberRepository;
        this.pollQuestionRepository = pollQuestionRepository;
        this.pollQuestionOptionRepository = pollQuestionOptionRepository;
        this.telegramApi = telegramApi;
    }

    public void sendMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sender.execute(sendMessage);
    }


    public void handleResponse(long chatId, Message message) {
        if (AdminCommands.VIEW_SCOREBOARD.getCommand().equals(message.getText())) {
            List<Member> members = memberRepository.findAll();
            StringBuilder scoreboard = new StringBuilder();
            members.forEach(member -> {
                String row = member.getUsername() + " - " + member.getPoints();
                scoreboard.append(row).append("\n");
            });
            this.sendMessage(chatId, scoreboard.toString());
        } else if (message.getPoll() != null) {
            Poll poll = message.getPoll();
            this.savePollQuestionAndOptions(poll);
            this.publishQuestionToGroup(poll);
        } else {
            unexpectedMessage(chatId);
        }
    }

    private void savePollQuestionAndOptions(Poll poll) {
        PollQuestion pollQuestion = PollQuestion.builder()
                .question(poll.getQuestion())
                .type(poll.getType())
                .build();
        pollQuestionRepository.save(pollQuestion);
        List<PollQuestionOption> options = new ArrayList<>();
        Integer correctAnswerIndex = poll.getCorrectOptionId();
        for (int i = 0; i < poll.getOptions().size(); i++) {
            PollQuestionOption pollQuestionOption = PollQuestionOption.builder()
                    .pollId(pollQuestion.getId())
                    .optionName(poll.getOptions().get(i).getText())
                    .isCorrectAnswer(false)
                    .build();
            if (correctAnswerIndex == i) {
                pollQuestionOption.setCorrectAnswer(true);
            }
            options.add(pollQuestionOption);
        }
        pollQuestionOptionRepository.saveAll(options);
    }

    private void publishQuestionToGroup(Poll poll) {
        List<String> options = new ArrayList<>();
        for (int i = 0; i < poll.getOptions().size(); i++) {
            options.add(poll.getOptions().get(i).getText());
        }
        telegramApi.createPoll(PollRequestDto.builder()
                .chat_id(String.valueOf(Constants.GROUP_CHAT_ID))
                .question(poll.getQuestion())
                .options(options)
                .correct_option_id(poll.getCorrectOptionId())
                .is_anonymous(false)
                .type("quiz")
                .build()
        );
    }

    private void unexpectedMessage(long chatId) {
        sendMessage(chatId, "I did not expect that.");
    }

}
