package com.awesomengwin.kingfisher.lyrics;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Lyrics(
        String trackId,
        List<LyricsLine> lines
) {

    public Lyrics withTranslationLines(List<TranslationLine> translationLines) {
        Map<Long, String> byStartTimeMs = translationLines.stream()
                .collect(Collectors.toMap(
                        TranslationLine::startTimeMs,
                        TranslationLine::words));

        return new Lyrics(trackId, lines.stream()
                .map(l -> l.withTranslatedWords(byStartTimeMs.get(l.startTimeMs())))
                .toList());
    }

    public TranslationStatus translationStatus() {
        boolean isPartial = lines.stream().anyMatch(l -> l.translatedWords() != null
                && !l.translatedWords().isBlank());

        if (!isPartial) {
            return TranslationStatus.NONE;
        }

        boolean completed = lines.stream().allMatch(l -> l.translatedWords() != null
                && !l.translatedWords().isBlank());

        return completed ? TranslationStatus.COMPLETED : TranslationStatus.PARTIAL;
    }
}
