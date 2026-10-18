package com.awesomengwin.kingfisher.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SpotifyTransferPlaybackRequest(
        @JsonProperty("device_ids")
        List<String> deviceIds
) {

    public SpotifyTransferPlaybackRequest(String deviceId) {
        this(List.of(deviceId));
    }
}
