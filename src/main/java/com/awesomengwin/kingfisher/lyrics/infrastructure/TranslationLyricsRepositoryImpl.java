package com.awesomengwin.kingfisher.lyrics.infrastructure;

import com.awesomengwin.kingfisher.lyrics.TranslationLyrics;
import com.awesomengwin.kingfisher.lyrics.TranslationLyricsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TranslationLyricsRepositoryImpl implements TranslationLyricsRepository {

    private final TranslationLyricsJpaRepository repository;
    private final TranslationLyricsEntityMapper mapper;

    public TranslationLyricsRepositoryImpl(TranslationLyricsJpaRepository repository, TranslationLyricsEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<TranslationLyrics> findTranslationLyrics(String trackId, String userId) {
        return repository.findByTrackIdAndUserId(trackId, userId).map(mapper::toDomain);
    }

    @Override
    public void saveTranslationLyrics(TranslationLyrics lyrics) {
        repository.save(mapper.toEntity(lyrics));
    }
}
