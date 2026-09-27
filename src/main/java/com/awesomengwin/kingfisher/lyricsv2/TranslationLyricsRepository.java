package com.awesomengwin.kingfisher.lyricsv2;

import java.util.Optional;

public interface TranslationLyricsRepository {

    Optional<TranslationLyrics> findTranslationLyrics(String trackId, String userId);

    void saveTranslationLyrics(TranslationLyrics lyrics);

}
