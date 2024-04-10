package com.tele.tuitionbot.service;

import com.tele.tuitionbot.entity.Member;
import com.tele.tuitionbot.repository.MemberRepository;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Optional;

import static com.tele.tuitionbot.util.Constants.DEFAULT_POINTS;
import static com.tele.tuitionbot.util.Constants.WELCOME_TEXT;

public class TuitionService {

    private final SilentSender sender;

    private final MemberRepository memberRepository;

    public TuitionService(SilentSender sender, MemberRepository memberRepository) {
        this.sender = sender;
        this.memberRepository = memberRepository;
    }

    public void handleNewMemberUpdates(long chatId, Message message) {
        List<User> newUsers = message.getNewChatMembers();
        newUsers.forEach(user -> {
            sendMessage(chatId, String.format(WELCOME_TEXT, user.getUserName()));
            Optional<Member> optionalMember = memberRepository.findByTelegramId(user.getId());
            if (optionalMember.isEmpty()) {
                Member newMember = Member.builder()
                        .telegramId(user.getId())
                        .username(user.getUserName())
                        .points(DEFAULT_POINTS)
                        .build();
                memberRepository.save(newMember);
            }
        });
    }

    public void handleLeaveMemberUpdates(Message message) {
        User leftMember = message.getLeftChatMember();
        Optional<Member> optionalMember = memberRepository.findByTelegramId(leftMember.getId());
        optionalMember.ifPresent(member -> memberRepository.deleteById((long) member.getId()));
    }

    public void sendMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sender.execute(sendMessage);
    }
}
