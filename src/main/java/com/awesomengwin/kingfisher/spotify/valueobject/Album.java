package com.awesomengwin.kingfisher.spotify.valueobject;

import java.util.List;

public record Album(
        String id,
        String name,
        String uri,
        List<Image> images
) {
}
