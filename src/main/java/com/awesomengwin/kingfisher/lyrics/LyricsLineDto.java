package com.awesomengwin.kingfisher.lyrics;

public record LyricsLineDto(
        Long startTimeMs,
        String words,
        Long endTimeMs,
        String translatedWords
) {

    public LyricsLineDto withTranslatedWords(String translatedWords) {
        return new LyricsLineDto(startTimeMs, words, endTimeMs, translatedWords);
    }
}
