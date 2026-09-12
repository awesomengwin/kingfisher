package com.awesomengwin.kingfisher.spotify.client;

import java.util.List;

public record PlaylistResponse(
        boolean collaborative,
        String description,
        String href,
        String name,
        String uri,
        List<Image> images,
        List<PlaylistItemRef> items
) {
    public record PlaylistItemRef(
            String href,
            int total
    ) {
    }
}
