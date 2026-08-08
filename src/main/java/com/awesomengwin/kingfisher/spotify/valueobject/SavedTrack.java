package com.awesomengwin.kingfisher.spotify.valueobject;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record SavedTrack(
        @JsonProperty("added_at")
        Instant addedAt,
        Track track
) {
}
