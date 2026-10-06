package com.awesomengwin.kingfisher.lyrics.jpa;

import com.awesomengwin.kingfisher.lyrics.*;
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

        Translation translation = translationRepository.findByTrackIdAndUserId(event.trackId(), event.userId())
                .orElseThrow(() -> new TranslationNotFoundException(event.trackId(), event.userId()));

        try {
            TranslateLyricsResponse resp = translateLyricsService.translate(
                    TranslateLyricsRequest.from(lyrics, event.userId()));

            List<TranslationLine> lines = resp.lines().stream()
                    .map(l -> new TranslationLine(l.startTimeMs(), l.translatedWords()))
                    .toList();

            translation.markCompleted(lines);
        } catch (Exception e) {
            translation.markFailed(e.getMessage());
        }

        translationRepository.save(translation);
    }
}
