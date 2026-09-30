package com.awesomengwin.kingfisher.lyrics;

public record LyricsLine(
        Long startTimeMs,
        String words,
        Long endTimeMs,
        String translatedWords
) {

    public LyricsLine(Long startTimeMs, String words, Long endTimeMs) {
        this(startTimeMs, words, endTimeMs, null);
    }

    public LyricsLine withTranslatedWords(String translatedWords) {
        return new LyricsLine(startTimeMs, words, endTimeMs, translatedWords);
    }
}
