package com.awesomengwin.kingfisher.lyricsv2;

import java.util.List;

public record TranslateLyricsRequest(
        List<TranslateLyricsLine> lines,
        String userId
) {

    public static TranslateLyricsRequest from(List<LyricsLine> lines, String userId) {
        List<TranslateLyricsLine> translateLyricsLines = lines.stream()
                .map(line -> new TranslateLyricsLine(line.startTimeMs(), line.words()))
                .toList();

        return new TranslateLyricsRequest(translateLyricsLines, userId);
    }

    public record TranslateLyricsLine(
            Long startTimeMs,
            String words
    ) {
    }
}
