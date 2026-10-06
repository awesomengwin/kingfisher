package com.awesomengwin.kingfisher.lyrics;

import com.awesomengwin.kingfisher.common.ApiClientNotFoundException;
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

        try {
            Lyrics lyrics = lyricsProvider.getLyrics(trackId);

            lyricsRepository.save(lyrics);

            return lyrics;
        } catch (ApiClientNotFoundException e) {
            return Lyrics.notFound(trackId);
        }
    }

    public Translation translateLyrics(String trackId, String userId) {
        if (!lyricsRepository.existsById(trackId)) {
            throw new LyricsNotFoundException(trackId);
        }

        Translation translation = new Translation(trackId, userId);

        translationRepository.save(translation);

        return translation;
    }

    public Translation getTranslation(String trackId, String userId) {
        return translationRepository.findByTrackIdAndUserId(trackId, userId)
                .orElseThrow(() -> new TranslationNotFoundException(trackId, userId));
    }
}
