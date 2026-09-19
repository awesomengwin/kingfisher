package com.awesomengwin.kingfisher.lyrics.impl;

import com.awesomengwin.kingfisher.common.ApiClientNotFoundException;
import com.awesomengwin.kingfisher.lyrics.*;
import com.awesomengwin.kingfisher.lyrics.client.LyricsApiResponse;
import com.awesomengwin.kingfisher.lyrics.client.LyricsClient;
import com.awesomengwin.kingfisher.lyrics.translator.LyricsTranslator;
import com.awesomengwin.kingfisher.lyrics.translator.LyricsTranslatorRequest;
import com.awesomengwin.kingfisher.lyrics.translator.LyricsTranslatorResponse;
import com.awesomengwin.kingfisher.lyrics.translator.LyricsTranslatorResponse.TranslatedLyricsLine;
import com.awesomengwin.kingfisher.spotify.SpotifyService;
import com.awesomengwin.kingfisher.spotify.client.Track;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class HttpClientThenDbLyricsService implements LyricsService {

    private final LyricsRepository lyricsRepository;
    private final LyricsClient lyricsClient;
    private final SpotifyService spotifyService;
    private final LyricsTranslator lyricsTranslator;

    public HttpClientThenDbLyricsService(LyricsRepository lyricsRepository, LyricsClient lyricsClient, SpotifyService spotifyService, LyricsTranslator lyricsTranslator) {
        this.lyricsRepository = lyricsRepository;
        this.lyricsClient = lyricsClient;
        this.spotifyService = spotifyService;
        this.lyricsTranslator = lyricsTranslator;
    }

    @Override
    public LyricsDto getLyrics(String trackId) {
        Optional<Lyrics> lyricsOpt = lyricsRepository.findByTrackId(trackId);

        if (lyricsOpt.isPresent()) {
            return new LyricsDto(lyricsOpt.get().getTrackId(), lyricsOpt.get().getLines(), lyricsOpt.get().getTranslateStatus());
        }

        try {
            LyricsApiResponse lyricsApiResponse = lyricsClient.getLyrics(trackId);

            List<LyricsLine> lyricsLines = lyricsApiResponse.lines().stream()
                    .map(line -> new LyricsLine(line.startTimeMs(), line.words(), line.endTimeMs()))
                    .toList();

            Lyrics lyrics = new Lyrics(trackId, lyricsLines);
            lyricsRepository.save(lyrics);

            return new LyricsDto(lyrics.getTrackId(), lyrics.getLines(), lyrics.getTranslateStatus());
        } catch (ApiClientNotFoundException e) {
            return new LyricsDto(trackId, Collections.emptyList(), null);
        }
    }

    @Override
    public LyricsDto translate(String trackId) {
        Lyrics lyrics = lyricsRepository.findByTrackId(trackId)
                .orElseThrow(() -> new RuntimeException("Lyrics of track %s could not be found".formatted(trackId)));

        Track track = spotifyService.getTrack(trackId);

        List<String> lines = lyrics.getLines().stream()
                .map(LyricsLine::words)
                .toList();

        var trackMetadata = new LyricsTranslatorRequest.TrackMetadata(
                track.name(),
                track.album().name(),
                track.artists().stream()
                        .map(Track.Artist::name)
                        .toList());

        LyricsTranslatorResponse translated = lyricsTranslator
                .translate(new LyricsTranslatorRequest(lines, trackMetadata));

        List<LyricsLine> mergedLyricsLine = getMergedLyricsLine(lyrics.getLines(), translated.lines());

        lyrics.updateLines(mergedLyricsLine);

        lyricsRepository.save(lyrics);

        return new LyricsDto(lyrics.getTrackId(), lyrics.getLines(), lyrics.getTranslateStatus());
    }

    private List<LyricsLine> getMergedLyricsLine(List<LyricsLine> source,
                                                 List<TranslatedLyricsLine> translated) {
        Map<Integer, String> byIndex = translated.stream()
                .collect(Collectors.toMap(TranslatedLyricsLine::index, TranslatedLyricsLine::translatedWords));

        if (byIndex.size() != source.size()) {
            throw new IllegalArgumentException("Expected %d translated lines, got %d"
                    .formatted(source.size(), byIndex.size()));
        }

        return IntStream.range(0, source.size())
                .mapToObj(i -> {
                    String translatedWords = byIndex.get(i);

                    LyricsLine line = source.get(i);

                    return new LyricsLine(line.startTimeMs(), line.words(), line.endTimeMs(), translatedWords);
                })
                .toList();
    }
}
