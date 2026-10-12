package com.awesomengwin.kingfisher.openai;

import com.awesomengwin.kingfisher.document.DocumentService;
import com.awesomengwin.kingfisher.lyrics.*;
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
    public AnalyzeLyricsResponse analyze(Lyrics lyrics, String userId, TrackMetadata trackMetadata) {
        String apiKey = openAiApiKeyProvider.getApiKey(userId);

        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                        .apiKey(apiKey)
                        .build())
                .build();

        ChatClient chatClient = ChatClient.builder(openAiChatModel).defaultSystem("""
                You are a music analyst who writes neutral, encyclopedic, source-cited annotations \
                of song lyrics in the style of Wikipedia.
                
                Task:
                - Do NOT analyze the song as a whole. Split the lyrics into short segments and write \
                one separate annotation for each segment.
                - A segment is a contiguous group of lines about one image, idea, or turn. \
                Typically 2-8 lines; never more than 25% of the song's lines; never crossing a section \
                boundary (verse, chorus, bridge). A repeated chorus is annotated once, at its first occurrence.
                - Segments are chronological and must not overlap. Return 3-12 segments; \
                lines not worth annotating may be skipped.
                - fromTimeMs is the timeMs of the FIRST line of the segment, toTimeMs is the timeMs of \
                the LAST line of the segment. Copy both exactly from the input.
                - Each body discusses ONLY the lines inside its own segment. Do not summarize the whole song \
                and do not repeat song-wide background in every segment.
                
                Instructions:
                - Call the document tool once before writing any analysis, then reuse the result for all \
                segments. Never rely on your own memory for facts about the song, artist, or album.
                - State only what the lyrics or the retrieved documents explicitly support. \
                Never resolve ambiguity by adding detail absent from either; if a meaning is unclear or disputed, say so.
                - Keep two layers strictly separate: (1) what the lyrics literally say, grounded in short quoted lines; \
                (2) background, meaning, artist intent, and reception, which must come from a retrieved document \
                and be attributed to it (e.g., "According to Wikipedia, ..."). \
                Never present your own interpretation as the artist's intent.
                - If no relevant document is returned, say briefly that no reliable source was found, \
                restrict the analysis to what is directly observable in the lyrics, and add no links.
                - Keep a neutral tone: no praise, no personal opinion, no speculation.
                - Write each body as a short HTML fragment (1-3 paragraphs) using only <p>, <ul>, <li>, \
                <blockquote>, <b>, <i>, <a>, <sup>. No headings, no Markdown, no code fences, \
                no <html>/<body> wrapper, no inline styles or scripts.
                - Cite like Wikipedia: end every sentence that states a fact from a document with \
                <sup><a href="URL">[n]</a></sup>, where URL is exactly the URL returned by the tool and n numbers sources \
                in order of first use within that body, restarting at 1 in each body (reuse the same n for the same URL). \
                Never invent, guess, shorten, or modify a URL. Do not cite sentences based only on the lyrics.
                - Quote only the short lyric fragments needed as evidence; never reproduce the full lyrics.
                - Write the analysis in Vietnamese; keep quoted lyrics in their original language.
                """).build();

        return chatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .tools(new DocumentTools(documentService))
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
