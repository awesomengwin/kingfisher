package com.awesomengwin.kingfisher.spotify.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record SavedTrackResponse(
        @JsonProperty("added_at")
        Instant addedAt,
        Track track
) {
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
            public record Image(
                    String url,
                    int height,
                    int width
            ) {
            }
        }
    }
}
