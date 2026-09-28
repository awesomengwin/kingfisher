package com.awesomengwin.kingfisher.lyrics.infrastructure;

import com.awesomengwin.kingfisher.lyrics.TranslationLyrics;
import com.awesomengwin.kingfisher.lyrics.TranslationLyricsLine;
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
    public Optional<TranslationLyrics> findTranslationLyrics(String trackId, String userId) {
        return repository.findByTrackIdAndUserId(trackId, userId)
                .map(entity -> new TranslationLyrics(
                        entity.getTrackId(),
                        entity.getUserId(),
                        entity.getLines().stream()
                                .map(vo -> new TranslationLyricsLine(
                                        vo.startTimeMs(),
                                        vo.words(),
                                        vo.translatedWords(),
                                        vo.endTimeMs()
                                )).toList()));
    }

    @Override
    public void saveTranslationLyrics(TranslationLyrics lyrics) {
        TranslationLyricsEntity translationLyricsEntity = new TranslationLyricsEntity(
                lyrics.trackId(),
                lyrics.userId(),
                lyrics.lines().stream()
                        .map(line -> new TranslationLyricsLineValueObject(
                                line.startTimeMs(),
                                line.words(),
                                line.translatedWords(),
                                line.endTimeMs()
                        )).toList());

        repository.save(translationLyricsEntity);
    }
}
