package com.awesomengwin.kingfisher.lyrics;

public record LyricsLineDto(
        Long startTimeMs,
        String words,
        Long endTimeMs,
        String translatedWords
) {

    public LyricsLineDto(Long startTimeMs, String words, Long endTimeMs) {
        this(startTimeMs, words, endTimeMs, null);
    }

    public LyricsLineDto withTranslatedWords(String translatedWords) {
        return new LyricsLineDto(startTimeMs, words, endTimeMs, translatedWords);
    }
}
