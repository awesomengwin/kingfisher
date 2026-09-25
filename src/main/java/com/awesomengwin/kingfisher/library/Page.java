package com.awesomengwin.kingfisher.library;

import java.util.List;

public record Page<T>(
        int page,
        int size,
        int totalPages,
        int totalElements,
        List<T> items
) {
}
