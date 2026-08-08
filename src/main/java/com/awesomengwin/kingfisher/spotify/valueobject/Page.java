package com.awesomengwin.kingfisher.spotify.valueobject;

import java.util.List;

public record Page<T>(
        int limit,
        int offset,
        int total,
        String next,
        String previous,
        List<T> items
) {
}
