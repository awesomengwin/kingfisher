package com.awesomengwin.kingfisher.lyrics.translator;

import java.util.List;

public record LyricsTranslatorResponse(List<TranslatedLyricsLine> lines) {
    public record TranslatedLyricsLine(Long startTimeMs, String translatedWords) {
    }
}
