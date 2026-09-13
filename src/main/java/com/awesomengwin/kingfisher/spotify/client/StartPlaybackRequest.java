package com.awesomengwin.kingfisher.spotify.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record StartPlaybackRequest(
        List<String> uris,
        @JsonProperty("context_uri")
        String contextUri,
        ContextOffset offset
) {
    public StartPlaybackRequest(String uri) {
        this(List.of(uri), null, null);
    }

    public StartPlaybackRequest(String contextUri, String uri) {
        this(null, contextUri, new ContextOffset(uri));
    }

    public record ContextOffset(
            String uri
    ) {
    }
}
