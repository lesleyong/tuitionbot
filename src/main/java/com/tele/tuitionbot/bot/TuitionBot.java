package com.tele.tuitionbot.bot;

import com.tele.tuitionbot.repository.MemberRepository;
import com.tele.tuitionbot.service.TuitionService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

@Component
public class TuitionBot extends AbilityBot {

    private final TuitionService tuitionService;

    public TuitionBot(Environment env, MemberRepository memberRepository) {
        super(env.getProperty("tuitionBotToken"), "TuitionButler_Bot");
        tuitionService = new TuitionService(silent, memberRepository);
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
        if (update.getMessage() != null) {
            // handle init members
            if (!update.getMessage().getNewChatMembers().isEmpty()) {
                tuitionService.handleNewMemberUpdates(getChatId(update), update.getMessage());
            }
            // handle leaving members
            if (update.getMessage().getLeftChatMember() != null) {
                tuitionService.handleLeaveMemberUpdates(update.getMessage());
            }
        }
        // handle poll response
    }
}
