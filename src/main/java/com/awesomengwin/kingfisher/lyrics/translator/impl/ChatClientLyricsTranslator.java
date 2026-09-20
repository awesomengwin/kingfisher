package com.awesomengwin.kingfisher.lyrics.translator.impl;

import com.awesomengwin.kingfisher.lyrics.translator.LyricsTranslator;
import com.awesomengwin.kingfisher.lyrics.translator.LyricsTranslatorRequest;
import com.awesomengwin.kingfisher.lyrics.translator.LyricsTranslatorResponse;
import com.awesomengwin.kingfisher.userpreferences.OpenAiApiKeyProvider;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

@Service
public class ChatClientLyricsTranslator implements LyricsTranslator {

    private final OpenAiApiKeyProvider openAiApiKeyProvider;

    public ChatClientLyricsTranslator(OpenAiApiKeyProvider openAiApiKeyProvider) {
        this.openAiApiKeyProvider = openAiApiKeyProvider;
    }

    @Override
    public LyricsTranslatorResponse translate(LyricsTranslatorRequest request) {
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
                .advisors(new SimpleLoggerAdvisor())
                .user(u -> u.text("""
                                Translate {trackName} by {artistNames} from the album {albumName}.
                                
                                Lyrics:
                                {lines}
                                """)
                        .param("trackName", request.trackMetadata().trackName())
                        .param("artistNames", String.join(", ", request.trackMetadata().artistNames()))
                        .param("albumName", request.trackMetadata().albumName())
                        .param("lines", request.getLinesPromptFormatted()))
                .call()
                .entity(LyricsTranslatorResponse.class);
    }
}
