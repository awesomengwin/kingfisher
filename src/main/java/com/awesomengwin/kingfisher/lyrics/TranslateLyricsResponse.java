package com.awesomengwin.kingfisher.lyrics;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record TranslateLyricsResponse(List<TranslateLyricsLine> lines) {

    public Map<Long, String> byStartTimeMs() {
        return lines.stream()
                .collect(Collectors.toMap(
                        TranslateLyricsLine::startTimeMs,
                        TranslateLyricsLine::translatedWords));
    }

    public record TranslateLyricsLine(
            Long startTimeMs,
            String translatedWords
    ) {
    }
}
