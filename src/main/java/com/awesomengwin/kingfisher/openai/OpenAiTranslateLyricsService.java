package com.awesomengwin.kingfisher.openai;

import com.awesomengwin.kingfisher.lyrics.TranslateLyricsRequest;
import com.awesomengwin.kingfisher.lyrics.TranslateLyricsResponse;
import com.awesomengwin.kingfisher.lyrics.TranslateLyricsService;
import com.awesomengwin.kingfisher.userpreferences.OpenAiApiKeyProvider;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenAiTranslateLyricsService implements TranslateLyricsService {

    private final OpenAiApiKeyProvider openAiApiKeyProvider;

    public OpenAiTranslateLyricsService(OpenAiApiKeyProvider openAiApiKeyProvider) {
        this.openAiApiKeyProvider = openAiApiKeyProvider;
    }

    @Override
    public TranslateLyricsResponse translate(TranslateLyricsRequest request) {
        String apiKey = openAiApiKeyProvider.getApiKey(request.userId());

        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                        .apiKey(apiKey)
                        .build())
                .build();

        ChatClient chatClient = ChatClient.builder(openAiChatModel).defaultSystem("""
                You are a professional literary translator specializing in translating English song lyrics \
                into natural, idiomatic Vietnamese.
                
                Instructions:
                - Never resolve ambiguity by adding detail absent from the source.
                - Preserve all punctuation, musical notation symbols, and blank lines.
                - Preserve line order and count exactly as given so one translated line per source line, \
                never merge, split, or omit lines.
                - Ensure consistency in capitalization between the translation and the original.
                """).build();

        return chatClient.prompt()
                .user(u -> u.text("""
                                Translate this track.
                                
                                Lyrics:
                                {lyrics}
                                """)
                        .param("lyrics", getLinesPromptFormatted(request.lines())))
                .call()
                .entity(TranslateLyricsResponse.class);
    }

    private String getLinesPromptFormatted(List<TranslateLyricsRequest.TranslateLyricsLine> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("lines must not be null or empty");
        }

        StringBuilder sb = new StringBuilder();

        for (TranslateLyricsRequest.TranslateLyricsLine line : lines) {
            Long startTimeMs = line.startTimeMs();
            String words = line.words();

            sb.append("[").append(startTimeMs).append("]")
                    .append(words == null || words.isBlank() ? "" : words)
                    .append("\n");
        }

        return sb.toString();
    }
}
