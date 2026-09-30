package com.awesomengwin.kingfisher.lyrics;

import java.util.Optional;

public interface TranslationRepository {

    Optional<Translation> findByTrackIdAndUserId(String trackId, String userId);

    void save(Translation lyrics);

}
