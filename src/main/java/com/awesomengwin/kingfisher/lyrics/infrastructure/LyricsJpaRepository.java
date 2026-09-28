package com.awesomengwin.kingfisher.lyrics.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LyricsJpaRepository extends JpaRepository<LyricsEntity, String> {
}
