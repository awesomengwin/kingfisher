package com.awesomengwin.kingfisher.spotifylegacy.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record PlaylistTrackResponse(
        @JsonProperty("added_at")
        Instant addedAt,
        Track item
) {
}
