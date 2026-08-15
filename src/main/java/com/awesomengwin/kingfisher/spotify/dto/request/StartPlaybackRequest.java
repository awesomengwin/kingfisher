package com.awesomengwin.kingfisher.spotify.dto.request;

import java.util.List;

public record StartPlaybackRequest(
        List<String> uris
) {
    public StartPlaybackRequest(String uri) {
        this(List.of(uri));
    }
}
