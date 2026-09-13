package com.awesomengwin.kingfisher.spotify.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record SavedTrackResponse(
        @JsonProperty("added_at")
        Instant addedAt,
        Track track
) {
}
