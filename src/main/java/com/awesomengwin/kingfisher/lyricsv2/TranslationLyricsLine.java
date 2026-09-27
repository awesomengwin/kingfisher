package com.awesomengwin.kingfisher.lyricsv2;

public record TranslationLyricsLine(
        Long startTimeMs,
        String words,
        String translatedWords,
        Long endTimeMs
) {
}
