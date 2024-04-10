package com.tele.tuitionbot.bot;

import com.tele.tuitionbot.api.TelegramApi;
import com.tele.tuitionbot.repository.MemberRepository;
import com.tele.tuitionbot.repository.PollQuestionOptionRepository;
import com.tele.tuitionbot.repository.PollQuestionRepository;
import com.tele.tuitionbot.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

@Component
public class TuitionAdminBot extends AbilityBot {

    private final List<Long> adminIds;

    private final AdminService adminService;

    @Autowired
    private final TelegramApi telegramApi;

    public TuitionAdminBot(Environment env, MemberRepository memberRepository, PollQuestionRepository pollQuestionRepository, PollQuestionOptionRepository pollQuestionOptionRepository, TelegramApi telegramApi) {
        super(env.getProperty("tuitionAdminBotToken"), "TuitionAdmin");
        this.telegramApi = telegramApi;
        this.adminService = new AdminService(silent, memberRepository, pollQuestionRepository, pollQuestionOptionRepository, telegramApi);

        String adminValues = env.getProperty("adminTelegramId");
        this.adminIds = adminValues != null && !adminValues.isEmpty() ? Arrays.stream(adminValues.split(",")).map(Long::valueOf).collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public long creatorId() {
        return 0;
    }

    @Override
    public void onRegister() {
    }

    @Override
    public void onUpdateReceived(Update update) {
        // only allow processing of messages from authorized users
        if (adminIds.contains(update.getMessage().getFrom().getId())) {
            adminService.handleResponse(getChatId(update), update.getMessage());
        } else {
            adminService.sendMessage(getChatId(update), "Not authorized.");
        }
    }
}
