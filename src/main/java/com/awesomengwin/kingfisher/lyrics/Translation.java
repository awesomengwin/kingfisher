package com.awesomengwin.kingfisher.lyrics;

import java.util.List;

public record Translation(
        String trackId,
        String userId,
        List<TranslationLine> lines
) {

    public static Translation from(String trackId, String userId, TranslateLyricsResponse resp) {
        if (resp == null) {
            throw new IllegalArgumentException("translate lyrics response must not be null");
        }

        if (resp.lines() == null) {
            throw new IllegalStateException("lines must not be null");
        }

        List<TranslationLine> lines = resp.lines().stream()
                .map(l -> new TranslationLine(l.startTimeMs(), l.translatedWords()))
                .toList();

        return new Translation(trackId, userId, lines);
    }
}
