package com.birdseyeapi.birdseyeapi.AI.Summarize;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.birdseyeapi.birdseyeapi.AI.OpenAI.APIChatMessageRole;
import com.birdseyeapi.birdseyeapi.AI.OpenAI.APIChatMessage;
import com.birdseyeapi.birdseyeapi.AI.OpenAI.APIChatRequest;
import com.birdseyeapi.birdseyeapi.AI.OpenAI.APIChatResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OpenAISummarizer implements AISummarizer {
    private static final String OpenAIEndpoint = System.getenv("OPENAI_CHAT_ENDPOINT");
    private static final String OpenAIAPIKey = System.getenv("OPENAI_API_KEY");
    private static final String OpenAIModel = "gpt-4.1-mini";
    private static final int MaxPromptTextLength = 3000;
    private final HttpClient httpClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public OpenAISummarizer() {
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public String summarize(String text) throws IOException, InterruptedException {
        List<APIChatMessage> messages = new ArrayList<>();
        // There is 4097 tokens limit when using gpt-3.5-turbo, thus a prompt must be
        // short.
        String prompt = String.format("""
                    次の文章を日本語で要約してください。
                    なお、要約結果の文章は200文字以内に収まるように調整してください。
                    ---
                    %s
                """, text);
        if (prompt.length() > MaxPromptTextLength) {
            prompt = prompt.substring(0, MaxPromptTextLength);
        }
        messages.add(new APIChatMessage(APIChatMessageRole.User.GetStrName(), prompt));
        APIChatRequest reqBody = new APIChatRequest(OpenAIModel, messages);

        final HttpRequest req = HttpRequest
                .newBuilder(URI.create(OpenAIEndpoint))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + OpenAIAPIKey)
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(reqBody)))
                .build();

        log.info("textBeforeSummarizedLength: " + text.length());

        final HttpResponse<String> res = httpClient.send(req, BodyHandlers.ofString());
        String resBody = res.body();

        final APIChatResponse result = mapper.readValue(resBody, APIChatResponse.class);
        return result.getChoices().get(0).getMessage().getContent();
    }

}
