package com.awesomengwin.kingfisher.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotifyUserPlaylist(
        String name,
        Owner owner,
        TracksRef items
) {
    public record Owner(
            @JsonProperty("display_name")
            String displayName
    ) {
    }

    public record TracksRef(Integer total) {
    }
}
