package com.awesomengwin.kingfisher.lyrics.infrastructure;

import com.awesomengwin.kingfisher.lyrics.TranslationLine;
import com.awesomengwin.kingfisher.lyrics.TranslationLyrics;
import com.awesomengwin.kingfisher.lyrics.TranslationLyricsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TranslationLyricsRepositoryImpl implements TranslationLyricsRepository {

    private final TranslationLyricsJpaRepository repository;

    public TranslationLyricsRepositoryImpl(TranslationLyricsJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<TranslationLyrics> findByTrackIdAndUserId(String trackId, String userId) {
        return repository.findById(new TranslationLyricsEntity.Id(trackId, userId))
                .map(e -> new TranslationLyrics(
                        e.getId().trackId(),
                        e.getId().userId(),
                        e.getLines().stream()
                                .map(l -> new TranslationLine(l.startTimeMs(), l.translatedWords()))
                                .toList()));
    }

    @Override
    public void save(TranslationLyrics lyrics) {
        TranslationLyricsEntity entity = new TranslationLyricsEntity(
                lyrics.trackId(),
                lyrics.userId(),
                lyrics.lines().stream()
                        .map(line -> new TranslationLyricsEntity.Line(
                                line.startTimeMs(),
                                line.words()
                        )).toList());

        repository.save(entity);
    }
}
