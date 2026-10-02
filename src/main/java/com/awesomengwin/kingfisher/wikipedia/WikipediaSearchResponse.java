package com.awesomengwin.kingfisher.wikipedia;

import java.util.List;
import java.util.OptionalLong;

public record WikipediaSearchResponse(Query query) {

    public OptionalLong getPageId() {
        checkForNull();

        return query.search().stream()
                .mapToLong(WikipediaSearchResult::pageId)
                .findFirst();
    }

    private void checkForNull() {
        if (query == null) {
            throw new IllegalStateException("query must not be null");
        }

        if (query.search() == null) {
            throw new IllegalStateException("search must not be null");
        }
    }

    public record Query(List<WikipediaSearchResult> search) {
    }
}
