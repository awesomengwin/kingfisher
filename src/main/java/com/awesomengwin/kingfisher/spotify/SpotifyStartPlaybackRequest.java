package com.awesomengwin.kingfisher.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotifyStartPlaybackRequest(
        @JsonProperty("context_uri")
        String contextUri,
        ContextOffset offset
) {

    public SpotifyStartPlaybackRequest(String contextUri, String uri) {
        this(contextUri, new ContextOffset(uri));
    }

    public record ContextOffset(String uri) {
    }
}
