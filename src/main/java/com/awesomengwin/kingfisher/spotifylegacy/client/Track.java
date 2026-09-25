package com.awesomengwin.kingfisher.spotifylegacy.client;

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
    public record Artist(
            String id,
            String name,
            String uri
    ) {
    }

    public record Album(
            String id,
            String name,
            String uri,
            List<Image> images
    ) {
    }
}
