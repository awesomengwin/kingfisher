package com.awesomengwin.kingfisher.lyrics;

import java.util.List;

public record LyricsTranslationDto(
        String trackId,
        List<LyricsTranslationLine> lines,
        TranslateStatus translateStatus
) {
    public record LyricsTranslationLine(
            Long startTimeMs,
            String words,
            Long endTimeMs,
            String translatedWords
    ) {
    }
}
