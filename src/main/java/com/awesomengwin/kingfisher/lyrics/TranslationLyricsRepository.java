package com.awesomengwin.kingfisher.lyrics;

import java.util.Optional;

public interface TranslationLyricsRepository {

    Optional<TranslationLyrics> findByTrackIdAndUserId(String trackId, String userId);

    void save(TranslationLyrics lyrics);

}
