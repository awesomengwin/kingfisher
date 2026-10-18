package com.awesomengwin.kingfisher.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotifyCurrentPlayback(
        SpotifyDevice device,
        @JsonProperty("is_playing")
        boolean isPlaying
) {
}
