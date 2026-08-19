package com.awesomengwin.kingfisher.lyrics;

public record LyricsLine(
        Long startTimeMs,
        String words,
        Long endTimeMs
) {
}
