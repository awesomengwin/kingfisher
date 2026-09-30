package com.awesomengwin.kingfisher.lyrics.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationLyricsJpaRepository
        extends JpaRepository<TranslationLyricsEntity, TranslationLyricsEntity.Id> {
}
