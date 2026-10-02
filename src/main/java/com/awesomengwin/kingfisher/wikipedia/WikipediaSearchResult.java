package com.awesomengwin.kingfisher.wikipedia;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WikipediaSearchResult(
        @JsonProperty("pageid")
        Long pageId
) {
}
