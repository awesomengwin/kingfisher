package com.awesomengwin.kingfisher.spotify.client;

import java.util.List;

public record PlaylistResponse(
        boolean collaborative,
        String description,
        String href,
        String name,
        String id,
        String uri,
        List<Image> images,
        PlaylistItemRef items
) {
    public record PlaylistItemRef(
            String href,
            int total
    ) {
    }
}
