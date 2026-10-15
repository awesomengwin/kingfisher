package com.awesomengwin.kingfisher.lyrics;

public final class LyricsFormatter {

    public static String formatLines(Lyrics lyrics) {
        StringBuilder sb = new StringBuilder();

        for (LyricsLine line : lyrics.lines()) {
            Long startTimeMs = line.startTimeMs();
            String words = line.words();

            sb.append("[").append(startTimeMs).append("]")
                    .append(words == null || words.isBlank() ? "" : words)
                    .append("\n");
        }

        return sb.toString();
    }

    private LyricsFormatter() {
    }
}
