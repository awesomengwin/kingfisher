package com.awesomengwin.kingfisher.lyrics;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record LyricsDto(
        String trackId,
        List<LyricsLineDto> lines,
        boolean translated
) {

    public LyricsDto(String trackId, List<LyricsLineDto> lines) {
        this(trackId, lines, false);
    }

    public static LyricsDto notFound(String trackId) {
        return new LyricsDto(trackId, List.of());
    }

    public LyricsDto withTranslationLines(List<TranslationLine> translationLines) {
        if (translationLines == null) {
            throw new IllegalArgumentException("translation lines must not be null");
        }

        Map<Long, String> byStartTimeMs = translationLines.stream()
                .collect(Collectors.toMap(
                        TranslationLine::startTimeMs,
                        TranslationLine::translatedWords));

        return new LyricsDto(trackId, lines.stream()
                .map(l -> l.withTranslatedWords(byStartTimeMs.get(l.startTimeMs())))
                .toList(), true);
    }
}
