package com.awesomengwin.kingfisher.lyrics;

import java.util.Optional;

public interface TranslationLyricsRepository {

    Optional<TranslationLyrics> findTranslationLyrics(String trackId, String userId);

    void saveTranslationLyrics(TranslationLyrics lyrics);

}
