package com.awesomengwin.kingfisher.lyrics.jpa;

import com.awesomengwin.kingfisher.lyrics.TranslationLine;
import com.awesomengwin.kingfisher.lyrics.Translation;
import com.awesomengwin.kingfisher.lyrics.TranslationRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TranslationRepositoryImpl implements TranslationRepository {

    private final TranslationJpaRepository repository;

    public TranslationRepositoryImpl(TranslationJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Translation> findByTrackIdAndUserId(String trackId, String userId) {
        return repository.findById(new TranslationEntity.Id(trackId, userId))
                .map(e -> Translation.reconstitute(
                        e.getId().trackId(),
                        e.getId().userId(),
                        e.getLines().stream()
                                .map(l -> new TranslationLine(l.startTimeMs(), l.translatedWords()))
                                .toList(),
                        e.getStatus()));
    }

    @Override
    public void save(Translation translation) {
        if (translation == null) {
            throw new IllegalArgumentException("translation must not be null");
        }

        if (translation.getLines() == null) {
            throw new IllegalStateException("lines must not be null");
        }

        TranslationEntity entity = new TranslationEntity(
                translation.getTrackId(),
                translation.getUserId(),
                translation.getLines().stream()
                        .map(line -> new TranslationEntity.Line(
                                line.startTimeMs(),
                                line.translatedWords()
                        )).toList(),
                translation.getStatus());

        for (Object event : translation.pullDomainEvents()) {
            entity.addDomainEvent(event);
        }

        repository.save(entity);
    }
}
