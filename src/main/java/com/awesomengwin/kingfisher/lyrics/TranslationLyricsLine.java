package com.awesomengwin.kingfisher.lyrics;

public record TranslationLyricsLine(
        Long startTimeMs,
        String words,
        String translatedWords,
        Long endTimeMs
) {
}
