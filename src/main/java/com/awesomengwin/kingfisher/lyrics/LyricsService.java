package com.awesomengwin.kingfisher.lyrics;

import org.springframework.stereotype.Service;

@Service
public class LyricsService {

    private final LyricsRepository lyricsRepository;
    private final LyricsProvider lyricsProvider;
    private final TranslationRepository translationRepository;

    public LyricsService(LyricsRepository lyricsRepository, LyricsProvider lyricsProvider,
                         TranslationRepository translationRepository) {
        this.lyricsRepository = lyricsRepository;
        this.lyricsProvider = lyricsProvider;
        this.translationRepository = translationRepository;
    }

    public Lyrics getLyrics(String trackId, String userId) {
        Lyrics savedLyrics = lyricsRepository.findById(trackId).orElse(null);

        if (savedLyrics != null) {
            Translation translation = translationRepository
                    .findByTrackIdAndUserId(trackId, userId).orElse(null);

            if (translation != null) {
                return savedLyrics.withTranslationLines(translation.getLines());
            }

            return savedLyrics;
        }

        Lyrics lyrics = lyricsProvider.getLyrics(trackId);

        lyricsRepository.save(lyrics);

        return lyrics;
    }

    public void translateLyrics(String trackId, String userId) {
        translationRepository.save(new Translation(trackId, userId));
    }

    public Translation getTranslation(String trackId, String userId) {
        return translationRepository.findByTrackIdAndUserId(trackId, userId)
                .orElseThrow(() -> new TranslationNotFoundException(trackId, userId));
    }
}
