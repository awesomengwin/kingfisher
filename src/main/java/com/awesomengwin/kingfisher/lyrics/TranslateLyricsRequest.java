package com.awesomengwin.kingfisher.lyrics;

import java.util.List;

public record TranslateLyricsRequest(
        List<Line> lines,
        String userId
) {

    public static TranslateLyricsRequest from(Lyrics lyrics, String userId) {
        if (lyrics == null) {
            throw new IllegalArgumentException("lyrics must not be null");
        }

        if (lyrics.lines() == null) {
            throw new IllegalStateException("lines must not be null");
        }

        List<Line> lines = lyrics.lines().stream()
                .map(l -> new Line(l.startTimeMs(), l.words()))
                .toList();

        return new TranslateLyricsRequest(lines, userId);
    }

    public record Line(Long startTimeMs, String words) {
    }
}
