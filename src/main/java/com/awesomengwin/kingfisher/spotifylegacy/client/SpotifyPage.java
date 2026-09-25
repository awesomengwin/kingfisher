package com.awesomengwin.kingfisher.spotifylegacy.client;

import java.util.List;

public record SpotifyPage<T>(
        int limit,
        int offset,
        int total,
        String next,
        String previous,
        List<T> items
) {
    public int currentPage() {
        return offset / limit + 1;
    }

    public int totalPages() {
        return (total + limit - 1) / limit;
    }
}
