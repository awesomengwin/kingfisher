package com.awesomengwin.kingfisher.lyrics.jpa;

import com.awesomengwin.kingfisher.lyrics.Lyrics;
import com.awesomengwin.kingfisher.lyrics.LyricsLine;
import com.awesomengwin.kingfisher.lyrics.LyricsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LyricsRepositoryImpl implements LyricsRepository {

    private final LyricsJpaRepository repository;

    public LyricsRepositoryImpl(LyricsJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Lyrics> findById(String trackId) {
        return repository.findById(trackId)
                .map(e -> new Lyrics(
                        e.getTrackId(),
                        e.getLines().stream()
                                .map(l -> new LyricsLine(
                                        l.startTimeMs(),
                                        l.words(),
                                        l.endTimeMs()))
                                .toList()));
    }

    @Override
    public void save(Lyrics lyrics) {
        LyricsEntity entity = new LyricsEntity(
                lyrics.trackId(),
                lyrics.lines().stream()
                        .map(l -> new LyricsEntity.Line(
                                l.startTimeMs(),
                                l.words(),
                                l.endTimeMs()))
                        .toList());

        repository.save(entity);
    }
}
