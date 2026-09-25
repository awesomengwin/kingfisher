package com.awesomengwin.kingfisher.spotifylegacy.web;

public record PaginationRequest(
        Integer page,
        Integer size
) {
    public PaginationRequest {
        page = page == null ? 1 : page;
        size = size == null ? 20 : size;
    }

    public int limit() {
        return size;
    }

    public int offset() {
        return (page - 1) * size;
    }
}
