package com.awesomengwin.kingfisher.lyrics;

import java.util.List;

public record LyricsDto(
        String trackId,
        List<LyricsLine> lines
) {
}
