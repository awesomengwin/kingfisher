package com.awesomengwin.kingfisher.lyrics;

import org.springframework.stereotype.Service;

@Service
public class LyricsService {

    private final LyricsRepository lyricsRepository;
    private final LyricsProvider lyricsProvider;
    private final TranslationRepository translationRepository;
    private final TranslateLyricsService translateLyricsService;

    public LyricsService(LyricsRepository lyricsRepository, LyricsProvider lyricsProvider,
                         TranslationRepository translationRepository,
                         TranslateLyricsService translateLyricsService) {
        this.lyricsRepository = lyricsRepository;
        this.lyricsProvider = lyricsProvider;
        this.translationRepository = translationRepository;
        this.translateLyricsService = translateLyricsService;
    }

    public Lyrics getLyrics(String trackId, String userId) {
        Lyrics savedLyrics = lyricsRepository.findById(trackId).orElse(null);

        if (savedLyrics != null) {
            Translation translation = translationRepository
                    .findByTrackIdAndUserId(trackId, userId).orElse(null);

            if (translation != null) {
                return savedLyrics.withTranslationLines(translation.lines());
            }

            return savedLyrics;
        }

        Lyrics lyrics = lyricsProvider.getLyrics(trackId);

        lyricsRepository.save(lyrics);

        return lyrics;
    }

    public Lyrics translateLyrics(String trackId, String userId) {
        Lyrics lyrics = lyricsRepository.findById(trackId)
                .orElseThrow(() -> new LyricsNotFoundException(trackId));

        TranslateLyricsResponse resp = translateLyricsService.translate(
                TranslateLyricsRequest.from(lyrics, userId));

        Translation translation = Translation.from(trackId, userId, resp);

        translationRepository.save(translation);

        return lyrics.withTranslationLines(translation.lines());
    }
}
