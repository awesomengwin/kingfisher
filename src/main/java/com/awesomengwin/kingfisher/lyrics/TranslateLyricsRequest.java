package com.awesomengwin.kingfisher.lyrics;

import java.util.List;

public record TranslateLyricsRequest(
        List<TranslationLine> lines,
        String userId
) {

    public static TranslateLyricsRequest from(List<LyricsLine> lines, String userId) {
        return new TranslateLyricsRequest(
                lines.stream()
                        .map(l -> new TranslationLine(l.startTimeMs(), l.words()))
                        .toList(),
                userId);
    }
}
