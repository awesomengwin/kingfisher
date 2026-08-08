package com.awesomengwin.kingfisher.spotify.valueobject;

import java.util.List;

public record Track(
        String id,
        String name,
        String uri,
        Album album,
        List<Artist> artists
) {
}
