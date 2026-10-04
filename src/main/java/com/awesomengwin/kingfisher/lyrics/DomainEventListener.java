package com.awesomengwin.kingfisher.lyrics;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DomainEventListener {

    private final LyricsRepository lyricsRepository;
    private final TranslationRepository translationRepository;
    private final TranslateLyricsService translateLyricsService;

    public DomainEventListener(LyricsRepository lyricsRepository, TranslationRepository translationRepository,
                               TranslateLyricsService translateLyricsService) {
        this.lyricsRepository = lyricsRepository;
        this.translationRepository = translationRepository;
        this.translateLyricsService = translateLyricsService;
    }

    @ApplicationModuleListener
    public void handleTranslationCreated(TranslationCreated event) {
        Lyrics lyrics = lyricsRepository.findById(event.trackId())
                .orElseThrow(() -> new LyricsNotFoundException(event.trackId()));

        TranslateLyricsResponse resp = translateLyricsService.translate(
                TranslateLyricsRequest.from(lyrics, event.userId()));

        if (resp == null) {
            throw new IllegalArgumentException("translate lyrics response must not be null");
        }

        if (resp.lines() == null) {
            throw new IllegalStateException("lines must not be null");
        }

        List<TranslationLine> lines = resp.lines().stream()
                .map(l -> new TranslationLine(l.startTimeMs(), l.translatedWords()))
                .toList();

        Translation translation = translationRepository.findByTrackIdAndUserId(event.trackId(), event.userId())
                .orElseThrow(() -> new TranslationNotFoundException(event.trackId(), event.userId()));

        translation.markCompleted(lines);

        translationRepository.save(translation);
    }
}
