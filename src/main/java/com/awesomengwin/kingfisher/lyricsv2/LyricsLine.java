package com.awesomengwin.kingfisher.lyricsv2;

public record LyricsLine(
        Long startTimeMs,
        String words,
        Long endTimeMs
) {
}
