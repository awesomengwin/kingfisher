package com.awesomengwin.kingfisher.spotify.valueobject;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Track(
        String id,
        String name,
        String uri,
        Album album,
        List<Artist> artists,
        @JsonProperty("duration_ms")
        int durationMs
) {
    public String getDurationFormatted() {
        long durationSeconds = Math.round(durationMs / 1000.0);
        return "%d:%02d".formatted(
                durationSeconds / 60,
                durationSeconds % 60
        );
    }
}
