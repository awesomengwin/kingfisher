package com.awesomengwin.kingfisher.lyrics.jpa;

import com.awesomengwin.kingfisher.lyrics.Analysis;
import com.awesomengwin.kingfisher.lyrics.AnalysisAnnotation;
import com.awesomengwin.kingfisher.lyrics.AnalysisRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AnalysisRepositoryImpl implements AnalysisRepository {

    private final AnalysisJpaRepository repository;

    public AnalysisRepositoryImpl(AnalysisJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Analysis> findByTrackIdAndUserId(String trackId, String userId) {
        return repository.findById(new AnalysisEntity.Id(trackId, userId))
                .map(e -> Analysis.reconstitute(
                        e.getId().trackId(),
                        e.getId().userId(),
                        e.getAnnotations().stream()
                                .map(a -> new AnalysisAnnotation(a.fromTimeMs(), a.toTimeMs(), a.body()))
                                .toList(),
                        e.getStatus(),
                        e.getFailureReason()
                ));
    }

    @Override
    public void save(Analysis analysis) {
        if (analysis == null) {
            throw new IllegalArgumentException("analysis must not be null");
        }

        if (analysis.getAnnotations() == null) {
            throw new IllegalArgumentException("analysis annotations must not be null");
        }

        AnalysisEntity entity = new AnalysisEntity(
                analysis.getTrackId(),
                analysis.getUserId(),
                analysis.getAnnotations().stream()
                        .map(a -> new AnalysisEntity.Annotation(a.fromTimeMs(), a.toTimeMs(), a.body()))
                        .toList(),
                analysis.getStatus(),
                analysis.getFailureReason());

        for (Object event : analysis.pullDomainEvents()) {
            entity.addDomainEvent(event);
        }

        repository.save(entity);
    }
}
