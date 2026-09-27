package com.awesomengwin.kingfisher.lyricsv2;

import java.util.List;

public record TranslationLyrics(
        String trackId,
        String userId,
        List<TranslationLyricsLine> lines
) {
}
