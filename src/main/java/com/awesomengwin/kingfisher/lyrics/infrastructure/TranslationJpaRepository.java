package com.awesomengwin.kingfisher.lyrics.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationJpaRepository extends JpaRepository<TranslationEntity, TranslationEntity.Id> {
}
