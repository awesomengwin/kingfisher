package com.awesomengwin.kingfisher.spotify.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record SavedTrack(
        @JsonProperty("added_at")
        Instant addedAt,
        Track track
) {
}
