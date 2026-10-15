package com.awesomengwin.kingfisher.openai;

import com.awesomengwin.kingfisher.document.DocumentResponse;
import com.awesomengwin.kingfisher.document.DocumentService;
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

import java.util.Map;

@Service
public class OpenAiAnalyzeLyricsService implements AnalyzeLyricsService {

    private final OpenAiApiKeyProvider openAiApiKeyProvider;
    private final DocumentService documentService;

    @Value("classpath:/prompts/analyze-lyrics-prompt.st")
    private Resource analyzeLyricsPromptResource;

    public OpenAiAnalyzeLyricsService(OpenAiApiKeyProvider openAiApiKeyProvider, DocumentService documentService) {
        this.openAiApiKeyProvider = openAiApiKeyProvider;
        this.documentService = documentService;
    }

    @Override
    public AnalyzeLyricsResponse analyze(Lyrics lyrics, String userId, TrackMetadata trackMetadata) {
        String apiKey = openAiApiKeyProvider.getApiKey(userId);

        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                        .apiKey(apiKey)
                        .build())
                .build();

        DocumentResponse trackContextDocument = documentService.getDocument(
                trackMetadata.trackName() + " " + String.join(" ", trackMetadata.artistNames()));

        String analyzeLyricsPrompt = PromptTemplate.builder()
                .resource(analyzeLyricsPromptResource)
                .build()
                .render(Map.of("track_context", trackContextDocument.content()));

        ChatClient chatClient = ChatClient.builder(openAiChatModel)
                .defaultSystem(analyzeLyricsPrompt)
                .build();

        return chatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .user(u -> u.text("""
                                Analyze {trackName} by {artistNames} from the album {albumName}.
                                
                                Lyrics:
                                {lines}
                                """)
                        .param("trackName", trackMetadata.trackName())
                        .param("artistNames", String.join(", ", trackMetadata.artistNames()))
                        .param("albumName", trackMetadata.albumName())
                        .param("lines", LyricsFormatter.formatLines(lyrics)))
                .call()
                .entity(AnalyzeLyricsResponse.class);
    }
}
