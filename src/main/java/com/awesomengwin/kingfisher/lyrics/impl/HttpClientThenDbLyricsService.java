package com.awesomengwin.kingfisher.lyrics.impl;

import com.awesomengwin.kingfisher.lyrics.*;
import com.awesomengwin.kingfisher.lyrics.client.LyricsApiResponse;
import com.awesomengwin.kingfisher.lyrics.client.LyricsClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HttpClientThenDbLyricsService implements LyricsService {

    private final LyricsRepository lyricsRepository;
    private final LyricsClient lyricsClient;

    public HttpClientThenDbLyricsService(LyricsRepository lyricsRepository, LyricsClient lyricsClient) {
        this.lyricsRepository = lyricsRepository;
        this.lyricsClient = lyricsClient;
    }

    @Override
    public LyricsDto getLyrics(String trackId) {
        Optional<Lyrics> lyricsOpt = lyricsRepository.findByTrackId(trackId);

        if (lyricsOpt.isEmpty()) {
            LyricsApiResponse lyricsApiResponse = lyricsClient.getLyrics(trackId);

            if (lyricsApiResponse.error()) {
                throw new RuntimeException("Failed to fetch lyrics");
            }

            List<LyricsLine> lyricsLines = lyricsApiResponse.lines().stream()
                    .map(line -> new LyricsLine(line.startTimeMs(), line.words(), line.endTimeMs()))
                    .toList();

            Lyrics lyrics = new Lyrics(trackId, lyricsLines);
            lyricsRepository.save(lyrics);

            return new LyricsDto(lyrics.getTrackId(), lyrics.getLines());
        }

        return new LyricsDto(lyricsOpt.get().getTrackId(), lyricsOpt.get().getLines());
    }
}
