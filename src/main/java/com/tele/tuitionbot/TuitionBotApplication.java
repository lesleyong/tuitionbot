package com.tele.tuitionbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;

import java.util.Map;

@SpringBootApplication
public class TuitionBotApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(TuitionBotApplication.class, args);
		try {
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(ctx.getBean("tuitionBot", AbilityBot.class));

			TelegramBotsApi tuitionAdmin = new TelegramBotsApi(DefaultBotSession.class);
			tuitionAdmin.registerBot(ctx.getBean("tuitionAdminBot", AbilityBot.class));
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}

		App app = new App();

		new ApplicationStack(app, "ApplicationStack",
				StackProps.builder()
						.description("Tuition Bot Dev")
						.tags(Map.of("env", "dev"))
						.build()
		);

		app.synth();
	}

}
