package com.awesomengwin.kingfisher.spotify;

import java.util.List;

public record SpotifyPage<T>(
        int limit,
        int offset,
        int total,
        List<T> items
) {
}
