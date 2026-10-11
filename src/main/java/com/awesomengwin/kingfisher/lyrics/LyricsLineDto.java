package com.awesomengwin.kingfisher.lyrics;

public record LyricsLineDto(
        Long startTimeMs,
        String words,
        Long endTimeMs,
        String translatedWords,
        String annotation
) {

    public LyricsLineDto(Long startTimeMs, String words, Long endTimeMs) {
        this(startTimeMs, words, endTimeMs, null, null);
    }

    public LyricsLineDto withTranslatedWords(String translatedWords) {
        return new LyricsLineDto(startTimeMs, words, endTimeMs, translatedWords, annotation);
    }

    public LyricsLineDto withAnnotation(String annotationBody) {
        return new LyricsLineDto(startTimeMs, words, endTimeMs, translatedWords, annotationBody);
    }
}
