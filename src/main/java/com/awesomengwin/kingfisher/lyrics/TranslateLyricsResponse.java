package com.awesomengwin.kingfisher.lyrics;

import java.util.List;

public record TranslateLyricsResponse(List<Line> lines) {

    public record Line(Long startTimeMs, String translatedWords) {
    }
}
