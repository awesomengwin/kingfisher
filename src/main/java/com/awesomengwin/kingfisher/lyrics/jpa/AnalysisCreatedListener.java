package com.awesomengwin.kingfisher.lyrics.jpa;

import com.awesomengwin.kingfisher.lyrics.*;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnalysisCreatedListener {

    private final LyricsRepository lyricsRepository;
    private final AnalysisRepository analysisRepository;
    private final AnalyzeLyricsService analyzeLyricsService;

    public AnalysisCreatedListener(LyricsRepository lyricsRepository, AnalysisRepository analysisRepository,
                                   AnalyzeLyricsService analyzeLyricsService) {
        this.lyricsRepository = lyricsRepository;
        this.analysisRepository = analysisRepository;
        this.analyzeLyricsService = analyzeLyricsService;
    }

    @ApplicationModuleListener
    public void handle(AnalysisCreated e) {
        Lyrics lyrics = lyricsRepository.findById(e.trackId())
                .orElseThrow(() -> new LyricsNotFoundException(e.trackId()));

        Analysis analysis = analysisRepository.findByTrackIdAndUserId(e.trackId(), e.userId())
                .orElseThrow(() -> new AnalysisNotFoundException(e.trackId(), e.userId()));

        try {
            AnalyzeLyricsResponse resp = analyzeLyricsService.analyze(
                    AnalyzeLyricsRequest.from(lyrics, e.userId()));

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
