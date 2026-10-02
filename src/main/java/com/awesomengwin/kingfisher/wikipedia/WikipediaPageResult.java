package com.awesomengwin.kingfisher.wikipedia;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WikipediaPageResult(
        @JsonProperty("pageid")
        Long pageId,
        String extract
) {
}
