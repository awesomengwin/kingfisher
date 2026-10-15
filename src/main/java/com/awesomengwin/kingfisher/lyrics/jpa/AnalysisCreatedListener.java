package com.awesomengwin.kingfisher.lyrics.jpa;

import com.awesomengwin.kingfisher.library.Artist;
import com.awesomengwin.kingfisher.library.LibraryService;
import com.awesomengwin.kingfisher.library.Track;
import com.awesomengwin.kingfisher.lyrics.*;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnalysisCreatedListener {

    private final LyricsRepository lyricsRepository;
    private final AnalysisRepository analysisRepository;
    private final AnalyzeLyricsService analyzeLyricsService;
    private final LibraryService libraryService;

    public AnalysisCreatedListener(LyricsRepository lyricsRepository, AnalysisRepository analysisRepository,
                                   AnalyzeLyricsService analyzeLyricsService, LibraryService libraryService) {
        this.lyricsRepository = lyricsRepository;
        this.analysisRepository = analysisRepository;
        this.analyzeLyricsService = analyzeLyricsService;
        this.libraryService = libraryService;
    }

    @ApplicationModuleListener
    public void handle(AnalysisCreated e) {
        Lyrics lyrics = lyricsRepository.findById(e.trackId())
                .orElseThrow(() -> new LyricsNotFoundException(e.trackId()));

        Analysis analysis = analysisRepository.findByTrackIdAndUserId(e.trackId(), e.userId())
                .orElseThrow(() -> new AnalysisNotFoundException(e.trackId(), e.userId()));

        Track track = libraryService.getTrack(e.trackId());

        try {
            TrackMetadata trackMetadata = new TrackMetadata(
                    track.name(),
                    track.album().name(),
                    track.artists().stream()
                            .map(Artist::name)
                            .toList());

            AnalyzeLyricsResponse resp = analyzeLyricsService.analyze(lyrics, e.userId(), trackMetadata);

            List<AnalysisAnnotation> annotations = resp.annotations().stream()
                    .map(a -> new AnalysisAnnotation(a.fromTimeMs(), a.toTimeMs(), a.body()))
                    .toList();

            analysis.markCompleted(annotations);
        } catch (Exception ex) {
            analysis.markFailed(ex.getMessage());
        }

        analysisRepository.save(analysis);
    }
}
