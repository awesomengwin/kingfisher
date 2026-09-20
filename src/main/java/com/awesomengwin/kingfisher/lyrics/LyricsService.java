package com.awesomengwin.kingfisher.lyrics;

public interface LyricsService {

    LyricsTranslationDto getLyrics(String trackId, String userId);

    LyricsTranslationDto translate(String trackId, String userId);
}
