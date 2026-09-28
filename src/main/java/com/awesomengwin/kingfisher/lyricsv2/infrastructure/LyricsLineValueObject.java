package com.awesomengwin.kingfisher.lyricsv2.infrastructure;

public record LyricsLineValueObject(
        Long startTimeMs,
        String words,
        Long endTimeMs
) {
}
