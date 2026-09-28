package com.awesomengwin.kingfisher.lyrics;

import java.util.List;

public record Lyrics(
        String trackId,
        List<LyricsLine> lines
) {
}
