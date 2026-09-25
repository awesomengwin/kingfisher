package com.awesomengwin.kingfisher.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record SpotifyUserSavedTrack(
        @JsonProperty("added_at")
        Instant addedAt,
        SpotifyTrack track
) {
}
