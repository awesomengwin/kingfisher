package com.awesomengwin.kingfisher.openai;

import com.awesomengwin.kingfisher.lyrics.*;
import com.awesomengwin.kingfisher.userpreferences.OpenAiApiKeyProvider;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class OpenAiTranslateLyricsService implements TranslateLyricsService {

    private final OpenAiApiKeyProvider openAiApiKeyProvider;

    @Value("classpath:/prompts/translate-lyrics-prompt.st")
    private Resource translateLyricsPromptResource;

    public OpenAiTranslateLyricsService(OpenAiApiKeyProvider openAiApiKeyProvider) {
        this.openAiApiKeyProvider = openAiApiKeyProvider;
    }

    @Override
    public TranslateLyricsResponse translate(Lyrics lyrics, String userId, TrackMetadata trackMetadata) {
        String apiKey = openAiApiKeyProvider.getApiKey(userId);

        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                        .apiKey(apiKey)
                        .build())
                .build();

        String translateLyricsPrompt = PromptTemplate.builder()
                .resource(translateLyricsPromptResource)
                .build().render();

        ChatClient chatClient = ChatClient.builder(openAiChatModel)
                .defaultSystem(translateLyricsPrompt).build();

        return chatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .user(u -> u.text("""
                                Translate {trackName} by {artistNames} from the album {albumName}.
                                
                                Lyrics:
                                {lines}
                                """)
                        .param("trackName", trackMetadata.trackName())
                        .param("artistNames", String.join(", ", trackMetadata.artistNames()))
                        .param("albumName", trackMetadata.albumName())
                        .param("lines", LyricsFormatter.formatLines(lyrics)))
                .call()
                .entity(TranslateLyricsResponse.class);
    }
}
