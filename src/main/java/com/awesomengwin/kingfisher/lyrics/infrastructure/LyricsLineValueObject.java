package com.awesomengwin.kingfisher.lyrics.infrastructure;

public record LyricsLineValueObject(
        Long startTimeMs,
        String words,
        Long endTimeMs
) {
}
