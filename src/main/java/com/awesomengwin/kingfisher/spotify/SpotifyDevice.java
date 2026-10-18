package com.awesomengwin.kingfisher.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotifyDevice(
        String id,
        String name,
        @JsonProperty("is_active")
        boolean isActive
) {
}
