package com.awesomengwin.kingfisher.wikipedia;

import java.util.List;

public record WikipediaSearchResponse(Query query) {

    public record Query(List<WikipediaSearchResult> search) {
    }
}
