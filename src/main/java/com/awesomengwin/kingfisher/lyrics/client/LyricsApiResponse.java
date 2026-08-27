package com.awesomengwin.kingfisher.lyrics.client;

import java.util.List;

public record LyricsApiResponse(
        Boolean error,
        String syncType,
        List<LyricsLine> lines
) {
    public record LyricsLine(
            Long startTimeMs,
            String words,
            Long endTimeMs
    ) {}
}
