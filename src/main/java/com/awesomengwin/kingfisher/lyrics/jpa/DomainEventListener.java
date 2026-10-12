package com.awesomengwin.kingfisher.lyrics.jpa;

import com.awesomengwin.kingfisher.library.Artist;
import com.awesomengwin.kingfisher.library.LibraryService;
import com.awesomengwin.kingfisher.library.Track;
import com.awesomengwin.kingfisher.lyrics.*;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DomainEventListener {

    private final LyricsRepository lyricsRepository;
    private final TranslationRepository translationRepository;
    private final TranslateLyricsService translateLyricsService;
    private final LibraryService libraryService;

    public DomainEventListener(LyricsRepository lyricsRepository, TranslationRepository translationRepository,
                               TranslateLyricsService translateLyricsService, LibraryService libraryService) {
        this.lyricsRepository = lyricsRepository;
        this.translationRepository = translationRepository;
        this.translateLyricsService = translateLyricsService;
        this.libraryService = libraryService;
    }

    @ApplicationModuleListener
    public void handleTranslationCreated(TranslationCreated event) {
        Lyrics lyrics = lyricsRepository.findById(event.trackId())
                .orElseThrow(() -> new LyricsNotFoundException(event.trackId()));

        Translation translation = translationRepository.findByTrackIdAndUserId(event.trackId(), event.userId())
                .orElseThrow(() -> new TranslationNotFoundException(event.trackId(), event.userId()));

        Track track = libraryService.getTrack(event.trackId());

        try {
            TrackMetadata trackMetadata = new TrackMetadata(
                    track.name(),
                    track.album().name(),
                    track.artists().stream()
                            .map(Artist::name)
                            .toList());

            TranslateLyricsResponse resp = translateLyricsService.translate(lyrics, event.userId(), trackMetadata);

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
