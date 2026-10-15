package com.awesomengwin.kingfisher.lyrics.jpa;

import com.awesomengwin.kingfisher.library.Artist;
import com.awesomengwin.kingfisher.library.LibraryService;
import com.awesomengwin.kingfisher.library.Track;
import com.awesomengwin.kingfisher.lyrics.*;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TranslationCreatedListener {

    private final LyricsRepository lyricsRepository;
    private final TranslationRepository translationRepository;
    private final TranslateLyricsService translateLyricsService;
    private final LibraryService libraryService;

    public TranslationCreatedListener(LyricsRepository lyricsRepository, TranslationRepository translationRepository,
                                      TranslateLyricsService translateLyricsService, LibraryService libraryService) {
        this.lyricsRepository = lyricsRepository;
        this.translationRepository = translationRepository;
        this.translateLyricsService = translateLyricsService;
        this.libraryService = libraryService;
    }

    @ApplicationModuleListener
    public void handleTranslationCreated(TranslationCreated e) {
        Lyrics lyrics = lyricsRepository.findById(e.trackId())
                .orElseThrow(() -> new LyricsNotFoundException(e.trackId()));

        Translation translation = translationRepository.findByTrackIdAndUserId(e.trackId(), e.userId())
                .orElseThrow(() -> new TranslationNotFoundException(e.trackId(), e.userId()));

        Track track = libraryService.getTrack(e.trackId());

        try {
            TrackMetadata trackMetadata = new TrackMetadata(
                    track.name(),
                    track.album().name(),
                    track.artists().stream()
                            .map(Artist::name)
                            .toList());

            TranslateLyricsResponse resp = translateLyricsService.translate(lyrics, e.userId(), trackMetadata);

            List<TranslationLine> lines = resp.lines().stream()
                    .map(l -> new TranslationLine(l.startTimeMs(), l.translatedWords()))
                    .toList();

            translation.markCompleted(lines);
        } catch (Exception ex) {
            translation.markFailed(ex.getMessage());
        }

        translationRepository.save(translation);
    }
}
