package com.awesomengwin.kingfisher.lyrics.infrastructure;

public record TranslationLyricsLineValueObject(
        Long startTimeMs,
        String words,
        String translatedWords,
        Long endTimeMs
) {
}
