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
                .map(e -> new Translation(
                        e.getId().trackId(),
                        e.getId().userId(),
                        e.getLines().stream()
                                .map(l -> new TranslationLine(l.startTimeMs(), l.translatedWords()))
                                .toList()));
    }

    @Override
    public void save(Translation lyrics) {
        TranslationEntity entity = new TranslationEntity(
                lyrics.trackId(),
                lyrics.userId(),
                lyrics.lines().stream()
                        .map(line -> new TranslationEntity.Line(
                                line.startTimeMs(),
                                line.words()
                        )).toList());

        repository.save(entity);
    }
}
