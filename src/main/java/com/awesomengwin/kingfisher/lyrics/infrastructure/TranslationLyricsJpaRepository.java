package com.awesomengwin.kingfisher.lyrics.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TranslationLyricsJpaRepository extends JpaRepository<TranslationLyricsEntity, Long> {

    Optional<TranslationLyricsEntity> findByTrackIdAndUserId(String trackId, String userId);

}
