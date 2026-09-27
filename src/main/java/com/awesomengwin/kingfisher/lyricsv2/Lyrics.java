package com.awesomengwin.kingfisher.lyricsv2;

import java.util.List;

public record Lyrics(
        String trackId,
        List<LyricsLine> lines
) {
}
