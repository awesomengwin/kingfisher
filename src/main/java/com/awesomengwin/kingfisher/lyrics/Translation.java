package com.awesomengwin.kingfisher.lyrics;

import java.util.List;

public record Translation(
        String trackId,
        String userId,
        List<TranslationLine> lines
) {
}
