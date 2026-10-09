package com.awesomengwin.kingfisher.lyrics;

import java.util.List;

public record AnalyzeLyricsResponse(List<Annotation> annotations) {

    public record Annotation(Long fromTimeMs, Long toTimeMs, String body) {
    }
}
