package com.awesomengwin.kingfisher.lyrics;

public record LyricsLineDto(
        Long startTimeMs,
        String words,
        Long endTimeMs,
        String translatedWords,
        String annotationBody,
        Long annotationFromTimeMs
) {

    public LyricsLineDto(Long startTimeMs, String words, Long endTimeMs) {
        this(startTimeMs, words, endTimeMs, null, null, null);
    }

    public LyricsLineDto withTranslatedWords(String translatedWords) {
        return new LyricsLineDto(startTimeMs, words, endTimeMs, translatedWords, annotationBody, annotationFromTimeMs);
    }

    public LyricsLineDto withAnnotation(String body, Long fromTimeMs) {
        return new LyricsLineDto(startTimeMs, words, endTimeMs, translatedWords, body, fromTimeMs);
    }
}
