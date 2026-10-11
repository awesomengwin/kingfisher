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

    public LyricsDto withAnnotatedLines(List<AnalysisAnnotation> analysisAnnotations) {
        if (analysisAnnotations == null) {
            throw new IllegalArgumentException("analysisAnnotations must not be null");
        }

        List<LyricsLineDto> annotatedLines = lines.stream()
                .map(l -> {
                    AnalysisAnnotation annotation = analysisAnnotations.stream()
                            .filter(a -> l.startTimeMs() >= a.fromTimeMs() && l.startTimeMs() <= a.toTimeMs())
                            .findFirst()
                            .orElse(null);

                    return annotation != null ? l.withAnnotation(annotation.body(), annotation.fromTimeMs()) : l;
                })
                .toList();

        return new LyricsDto(trackId, annotatedLines, translated);
    }
}
