package com.awesomengwin.kingfisher.spotifylegacy.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StartPlaybackRequest(
        @JsonProperty("context_uri")
        String contextUri,
        ContextOffset offset
) {
    public StartPlaybackRequest(String contextUri, String uri) {
        this(contextUri, new ContextOffset(uri));
    }

    public record ContextOffset(
            String uri
    ) {
    }
}
