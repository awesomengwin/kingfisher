package com.awesomengwin.kingfisher.lyrics.translator;

import java.util.List;

public record LyricsTranslatorRequest(List<LyricsLine> lines, TrackMetadata trackMetadata, String userId) {
    public record LyricsLine(Long startTimeMs, String words) {
    }

    public record TrackMetadata(
            String trackName,
            String albumName,
            List<String> artistNames
    ) {
    }

    public String getLinesPromptFormatted() {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("lines must not be null or empty");
        }

        StringBuilder sb = new StringBuilder();

        for (LyricsLine line : lines) {
            Long startTimeMs = line.startTimeMs();
            String words = line.words();

            sb.append("[").append(startTimeMs).append("]")
                    .append(words == null || words.isBlank() ? "" : words)
                    .append("\n");
        }

        return sb.toString();
    }
}
