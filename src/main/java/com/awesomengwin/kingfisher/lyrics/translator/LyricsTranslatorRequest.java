package com.awesomengwin.kingfisher.lyrics.translator;

import java.util.List;

public record LyricsTranslatorRequest(List<String> lines, TrackMetadata trackMetadata, String userId) {
    public record TrackMetadata(
            String trackName,
            String albumName,
            List<String> artistNames
    ) {
    }

    public String getNumberedLines() {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("lines must not be null or empty");
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < lines.size(); i++) {
            String words = lines.get(i);

            sb.append(i).append(": ")
                    .append(words == null || words.isBlank() ? "" : words)
                    .append("\n");
        }

        return sb.toString();
    }
}
