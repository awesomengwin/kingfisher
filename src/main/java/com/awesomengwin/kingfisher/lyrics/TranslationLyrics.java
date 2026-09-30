package com.awesomengwin.kingfisher.lyrics;

import java.util.List;

public record TranslationLyrics(
        String trackId,
        String userId,
        List<TranslationLine> lines
) {
}
