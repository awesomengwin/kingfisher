package com.awesomengwin.kingfisher.openai;

import com.awesomengwin.kingfisher.document.DocumentService;
import com.awesomengwin.kingfisher.lyrics.AnalyzeLyricsRequest;
import com.awesomengwin.kingfisher.lyrics.AnalyzeLyricsResponse;
import com.awesomengwin.kingfisher.lyrics.AnalyzeLyricsService;
import com.awesomengwin.kingfisher.userpreferences.OpenAiApiKeyProvider;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

@Service
public class OpenAiAnalyzeLyricsService implements AnalyzeLyricsService {

    private final OpenAiApiKeyProvider openAiApiKeyProvider;
    private final DocumentService documentService;

    public OpenAiAnalyzeLyricsService(OpenAiApiKeyProvider openAiApiKeyProvider, DocumentService documentService) {
        this.openAiApiKeyProvider = openAiApiKeyProvider;
        this.documentService = documentService;
    }

    @Override
    public AnalyzeLyricsResponse analyze(AnalyzeLyricsRequest request) {
        String apiKey = openAiApiKeyProvider.getApiKey(request.userId());

        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                        .apiKey(apiKey)
                        .build())
                .build();

        ChatClient chatClient = ChatClient.builder(openAiChatModel).defaultSystem("""
                You are a music analyst who writes neutral, encyclopedic, source-cited analyses of song lyrics \
                in the style of Wikipedia.
                
                Instructions:
                - Always call the document tool before writing any analysis. Never rely on your own memory \
                for facts about the song, artist, or album.
                - State only what the lyrics or the retrieved documents explicitly support. \
                Never resolve ambiguity by adding detail absent from either; if a meaning is unclear or disputed, say so.
                - Keep two layers strictly separate: (1) what the lyrics literally say, grounded in short quoted lines; \
                (2) background, meaning, artist intent, and reception, which must come from a retrieved document \
                and be attributed to it (e.g., "According to Wikipedia, ..."). \
                Never present your own interpretation as the artist's intent.
                - If no relevant document is returned, say briefly that no reliable source was found, \
                restrict the analysis to what is directly observable in the lyrics, and add no links.
                - Keep a neutral tone: no praise, no personal opinion, no speculation.
                - Write the body as HTML fragment only, using <h2>, <h3>, <p>, <ul>, <li>, <blockquote>, <b>, <i>, <a>, <sup>. \
                No Markdown, no code fences, no <html>/<body> wrapper, no inline styles or scripts.
                - Cite like Wikipedia: end every sentence that states a fact from a document with \
                <sup><a href="URL">[n]</a></sup>, where URL is exactly the URL returned by the tool and n numbers sources \
                in order of first use (reuse the same n for the same URL). \
                Never invent, guess, shorten, or modify a URL. Do not cite sentences based only on the lyrics.
                - Quote only the short lyric fragments needed as evidence; never reproduce the full lyrics.
                - Write the analysis in Vietnamese; keep quoted lyrics in their original language.
                """).build();

        return chatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .tools(new DocumentTools(documentService))
                .user(u -> u.text("""
                                Analyze this track.
                                
                                Lyrics:
                                {lyrics}
                                """)
                        .param("lyrics", formatLines(request)))
                .call()
                .entity(AnalyzeLyricsResponse.class);
    }

    public String formatLines(AnalyzeLyricsRequest request) {
        StringBuilder sb = new StringBuilder();

        for (AnalyzeLyricsRequest.Line line : request.lines()) {
            Long startTimeMs = line.startTimeMs();
            String words = line.words();

            sb.append("[").append(startTimeMs).append("]")
                    .append(words == null || words.isBlank() ? "" : words)
                    .append("\n");
        }

        return sb.toString();
    }
}
