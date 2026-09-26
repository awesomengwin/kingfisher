package com.awesomengwin.kingfisher.library;

public record Pageable(int page, int size) {

    public int limit() {
        return size;
    }

    public int offset() {
        return page * size;
    }
}
