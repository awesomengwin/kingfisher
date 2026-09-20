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
}
