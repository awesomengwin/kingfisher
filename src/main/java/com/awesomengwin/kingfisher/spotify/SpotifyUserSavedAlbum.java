package com.awesomengwin.kingfisher.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record SpotifyUserSavedAlbum(
        @JsonProperty("added_at")
        Instant addedAt,
        SpotifyAlbum album
) {
}
