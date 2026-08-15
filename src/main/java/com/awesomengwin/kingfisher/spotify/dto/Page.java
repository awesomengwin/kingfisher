package com.awesomengwin.kingfisher.spotify.dto;

import java.util.List;

public record Page<T>(
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
