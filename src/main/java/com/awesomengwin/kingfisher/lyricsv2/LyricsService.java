package com.awesomengwin.kingfisher.lyricsv2;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LyricsService {

    private final LyricsRepository lyricsRepository;
    private final LyricsProvider lyricsProvider;
    private final TranslationLyricsRepository translationLyricsRepository;
    private final TranslateLyricsService translateLyricsService;

    public LyricsService(LyricsRepository lyricsRepository, LyricsProvider lyricsProvider,
                         TranslationLyricsRepository translationLyricsRepository,
                         TranslateLyricsService translateLyricsService) {
        this.lyricsRepository = lyricsRepository;
        this.lyricsProvider = lyricsProvider;
        this.translationLyricsRepository = translationLyricsRepository;
        this.translateLyricsService = translateLyricsService;
    }

    public Lyrics getLyrics(String trackId) {
        Lyrics savedLyrics = lyricsRepository.findLyrics(trackId).orElse(null);

        if (savedLyrics != null) {
            return savedLyrics;
        }

        Lyrics lyrics = lyricsProvider.getLyrics(trackId);

        lyricsRepository.saveLyrics(lyrics);

        return lyrics;
    }

    public TranslationLyrics getTranslationLyrics(String trackId, String userId) {
        return translationLyricsRepository.findTranslationLyrics(trackId, userId)
                .orElseThrow(() -> new TranslationLyricsNotFoundException(trackId, userId));
    }

    public TranslationLyrics translateLyrics(String trackId, String userId) {
        Lyrics lyrics = lyricsRepository.findLyrics(trackId)
                .orElseThrow(() -> new LyricsNotFoundException(trackId));

        TranslateLyricsResponse translateLyricsResponse =
                translateLyricsService.translate(TranslateLyricsRequest.from(lyrics.lines(), userId));

        Map<Long, String> byStartTimeMs = translateLyricsResponse.byStartTimeMs();

        List<TranslationLyricsLine> translationLyricsLines = lyrics.lines().stream()
                .map(line -> new TranslationLyricsLine(
                        line.startTimeMs(),
                        line.words(),
                        byStartTimeMs.get(line.startTimeMs()),
                        line.endTimeMs()
                ))
                .toList();

        TranslationLyrics translationLyrics = new TranslationLyrics(trackId, userId, translationLyricsLines);

        translationLyricsRepository.saveTranslationLyrics(translationLyrics);

        return translationLyrics;
    }
}
