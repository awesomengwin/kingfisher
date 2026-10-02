package com.awesomengwin.kingfisher.wikipedia;

import java.util.Map;

public record WikipediaPageExtractResponse(Query query) {

    public WikipediaPageResult getPageResult(long pageId) {
        checkForNull();

        WikipediaPageResult result = query.pages().get(String.valueOf(pageId));

        if (result == null) {
            throw new IllegalStateException("There is no result for page ID %s".formatted(pageId));
        }

        return result;
    }

    private void checkForNull() {
        if (query == null) {
            throw new IllegalStateException("query must not be null");
        }

        if (query.pages() == null) {
            throw new IllegalStateException("pages must not be null");
        }
    }

    public record Query(Map<String, WikipediaPageResult> pages) {
    }
}
