package com.awesomengwin.kingfisher.lyrics.infrastructure;

import com.awesomengwin.kingfisher.lyrics.Lyrics;
import com.awesomengwin.kingfisher.lyrics.LyricsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LyricsRepositoryImpl implements LyricsRepository {

    private final LyricsJpaRepository repository;
    private final LyricsEntityMapper mapper;

    public LyricsRepositoryImpl(LyricsJpaRepository repository, LyricsEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Lyrics> findLyrics(String trackId) {
        return repository.findById(trackId).map(mapper::toDomain);
    }

    @Override
    public void saveLyrics(Lyrics lyrics) {
        repository.save(mapper.toEntity(lyrics));
    }
}
