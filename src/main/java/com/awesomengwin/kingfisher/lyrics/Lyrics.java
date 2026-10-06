package com.awesomengwin.kingfisher.lyrics;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Lyrics(
        String trackId,
        List<LyricsLine> lines,
        boolean translated
) {

    public Lyrics(String trackId, List<LyricsLine> lines) {
        this(trackId, lines, false);
    }

    public static Lyrics notFound(String trackId) {
        return new Lyrics(trackId, List.of(), false);
    }

    public Lyrics withTranslationLines(List<TranslationLine> translationLines) {
        if (translationLines == null) {
            throw new IllegalArgumentException("translation lines must not be null");
        }

        Map<Long, String> byStartTimeMs = translationLines.stream()
                .collect(Collectors.toMap(
                        TranslationLine::startTimeMs,
                        TranslationLine::translatedWords));

        return new Lyrics(trackId, lines.stream()
                .map(l -> l.withTranslatedWords(byStartTimeMs.get(l.startTimeMs())))
                .toList(), true);
    }
}
