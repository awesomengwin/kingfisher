package com.awesomengwin.kingfisher.spotify.dto.response;

import com.awesomengwin.kingfisher.spotify.dto.Image;

import java.util.List;

public record Album(
        String id,
        String name,
        String uri,
        List<Image> images
) {
}
