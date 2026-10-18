package com.awesomengwin.kingfisher.player;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrentPlayback(
        Device device,
        @JsonProperty("is_playing")
        boolean isPlaying
) {
}
