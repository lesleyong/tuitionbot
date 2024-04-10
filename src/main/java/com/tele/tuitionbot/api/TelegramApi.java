package com.tele.tuitionbot.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tele.tuitionbot.request.PollRequestDto;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Configuration
public class TelegramApi {

    private final String tuitionBotToken;

    private final String telegramBotBaseUrl;

    private final String contentType = "Content-Type";
    private final String accept = "accept";

    private final ObjectMapper objectMapper;

    public TelegramApi(Environment env) {
        this.tuitionBotToken = env.getProperty("tuitionBotToken");
        this.telegramBotBaseUrl = String.format("https://api.telegram.org/bot%s", this.tuitionBotToken);
        this.objectMapper = new ObjectMapper();
    }

    public void createPoll(PollRequestDto pollRequestDto) {
        try {
            String requestBody = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(pollRequestDto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(telegramBotBaseUrl + "/sendPoll"))
                    .header(contentType, MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .header(accept, MediaType.APPLICATION_JSON_VALUE)
                    .method(HttpMethod.POST.name(), HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
