package com.awesomengwin.kingfisher.lyrics;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TranslationRepository extends JpaRepository<Translation, Long> {

    Optional<Translation> findByTrackIdAndUserId(String trackId, String userId);
}
