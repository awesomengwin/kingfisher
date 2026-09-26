package com.awesomengwin.kingfisher.lyrics.impl;

import com.awesomengwin.kingfisher.common.ApiClientNotFoundException;
import com.awesomengwin.kingfisher.lyrics.*;
import com.awesomengwin.kingfisher.lyrics.client.LyricsApiResponse;
import com.awesomengwin.kingfisher.lyrics.client.LyricsClient;
import com.awesomengwin.kingfisher.lyrics.translator.LyricsTranslator;
import com.awesomengwin.kingfisher.lyrics.translator.LyricsTranslatorRequest;
import com.awesomengwin.kingfisher.lyrics.translator.LyricsTranslatorResponse;
import com.awesomengwin.kingfisher.library.LibraryService;
import com.awesomengwin.kingfisher.library.Track;
import com.awesomengwin.kingfisher.library.Artist;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HttpClientThenDbLyricsService implements LyricsService {

    private final LyricsRepository lyricsRepository;
    private final LyricsClient lyricsClient;
    private final LibraryService libraryService;
    private final LyricsTranslator lyricsTranslator;
    private final TranslationRepository translationRepository;

    public HttpClientThenDbLyricsService(LyricsRepository lyricsRepository, LyricsClient lyricsClient, LibraryService libraryService, LyricsTranslator lyricsTranslator, TranslationRepository translationRepository) {
        this.lyricsRepository = lyricsRepository;
        this.lyricsClient = lyricsClient;
        this.libraryService = libraryService;
        this.lyricsTranslator = lyricsTranslator;
        this.translationRepository = translationRepository;
    }

    @Override
    public LyricsTranslationDto getLyrics(String trackId, String userId) {
        Lyrics lyricsDb = lyricsRepository.findByTrackId(trackId)
                .orElse(null);

        if (lyricsDb != null) {
            Translation translation = translationRepository.findByTrackIdAndUserId(trackId, userId)
                    .orElse(null);

            if (translation != null) {
                return mergeLyricsAndTranslation(lyricsDb, translation);
            }

            return toLyricsWithoutTranslation(lyricsDb);
        }

        try {
            LyricsApiResponse lyricsApiResponse = lyricsClient.getLyrics(trackId);

            List<LyricsLine> lyricsLines = lyricsApiResponse.lines().stream()
                    .map(line -> new LyricsLine(line.startTimeMs(), line.words(), line.endTimeMs()))
                    .toList();

            Lyrics lyrics = new Lyrics(trackId, lyricsLines);
            lyricsRepository.save(lyrics);

            return toLyricsWithoutTranslation(lyrics);
        } catch (ApiClientNotFoundException e) {
            return new LyricsTranslationDto(trackId, Collections.emptyList(), null);
        }
    }

    @Override
    public LyricsTranslationDto translate(String trackId, String userId) {
        Lyrics lyrics = lyricsRepository.findByTrackId(trackId)
                .orElseThrow(() -> new RuntimeException("Lyrics of track %s could not be found".formatted(trackId)));

        Track track = libraryService.getTrack(trackId);

        List<LyricsTranslatorRequest.LyricsLine> lines = lyrics.getLines().stream()
                .map(line -> new LyricsTranslatorRequest.LyricsLine(line.startTimeMs(), line.words()))
                .toList();

        var trackMetadata = new LyricsTranslatorRequest.TrackMetadata(
                track.name(),
                track.album().name(),
                track.artists().stream()
                        .map(Artist::name)
                        .toList());

        LyricsTranslatorResponse translated = lyricsTranslator
                .translate(new LyricsTranslatorRequest(lines, trackMetadata, userId));

        if (lyrics.getLines().size() != translated.lines().size()) {
            throw new IllegalArgumentException("Expected %d translated lines, got %d"
                    .formatted(lyrics.getLines().size(), translated.lines().size()));
        }

        List<TranslatedLine> translatedLines = translated.lines().stream()
                .map(translatedLine -> new TranslatedLine(translatedLine.startTimeMs(), translatedLine.translatedWords()))
                .toList();

        Translation translation = translationRepository.findByTrackIdAndUserId(trackId, userId)
                .orElseGet(() -> new Translation(trackId, userId, translatedLines));

        translation.updateLines(translatedLines);

        translationRepository.save(translation);

        return mergeLyricsAndTranslation(lyrics, translation);
    }

    private LyricsTranslationDto mergeLyricsAndTranslation(Lyrics lyrics, Translation translation) {
        Map<Long, String> byStartTimeMs = translation.getLines().stream()
                .collect(Collectors.toMap(
                        TranslatedLine::startTimeMs,
                        TranslatedLine::translatedWords));

        List<LyricsTranslationDto.LyricsTranslationLine> lyricsTranslationLines = lyrics.getLines().stream()
                .map(line -> new LyricsTranslationDto.LyricsTranslationLine(
                        line.startTimeMs(),
                        line.words(),
                        line.endTimeMs(),
                        byStartTimeMs.get(line.startTimeMs())))
                .toList();

        return new LyricsTranslationDto(
                lyrics.getTrackId(),
                lyricsTranslationLines,
                translation.getTranslateStatus());
    }

    private LyricsTranslationDto toLyricsWithoutTranslation(Lyrics lyrics) {
        return new LyricsTranslationDto(
                lyrics.getTrackId(),
                lyrics.getLines().stream()
                        .map(line -> new LyricsTranslationDto.LyricsTranslationLine(
                                line.startTimeMs(),
                                line.words(),
                                line.endTimeMs(),
                                null
                        ))
                        .toList(),
                TranslateStatus.NONE
        );
    }
}
