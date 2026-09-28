package com.awesomengwin.kingfisher.lyrics.infrastructure;

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
    public Optional<Lyrics> findLyrics(String trackId) {
        return repository.findById(trackId)
                .map(entity -> new Lyrics(
                        entity.getTrackId(),
                        entity.getLines().stream()
                                .map(vo -> new LyricsLine(
                                        vo.startTimeMs(),
                                        vo.words(),
                                        vo.endTimeMs()))
                                .toList()));
    }

    @Override
    public void saveLyrics(Lyrics lyrics) {
        LyricsEntity lyricsEntity = new LyricsEntity(
                lyrics.trackId(),
                lyrics.lines().stream()
                        .map(line -> new LyricsLineValueObject(
                                line.startTimeMs(),
                                line.words(),
                                line.endTimeMs()))
                        .toList());

        repository.save(lyricsEntity);
    }
}
