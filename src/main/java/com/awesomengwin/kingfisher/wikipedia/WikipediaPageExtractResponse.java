package com.awesomengwin.kingfisher.wikipedia;

import java.util.Map;

public record WikipediaPageExtractResponse(Query query) {

    public record Query(Map<String, WikipediaPageResult> pages) {
    }
}
