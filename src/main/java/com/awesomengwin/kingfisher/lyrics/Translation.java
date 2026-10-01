package com.awesomengwin.kingfisher.lyrics;

import java.util.List;

public record Translation(
        String trackId,
        String userId,
        List<TranslationLine> lines
) {

    public static Translation from(String trackId, String userId, TranslateLyricsResponse resp) {
        List<TranslationLine> lines = resp.lines().stream()
                .map(l -> new TranslationLine(l.startTimeMs(), l.translatedWords()))
                .toList();

        return new Translation(trackId, userId, lines);
    }
}
