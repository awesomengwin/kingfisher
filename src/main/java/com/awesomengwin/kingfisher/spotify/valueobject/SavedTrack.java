package com.awesomengwin.kingfisher.spotify.valueobject;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SavedTrack(
        @JsonProperty("added_at")
        String addedAt,
        Track track
) {
}
