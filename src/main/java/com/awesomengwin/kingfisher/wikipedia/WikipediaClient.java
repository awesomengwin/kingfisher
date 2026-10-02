package com.awesomengwin.kingfisher.wikipedia;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface WikipediaClient {

    @GetExchange("?action=query&list=search&format=json")
    WikipediaSearchResponse search(@RequestParam("srsearch") String q);

    @GetExchange("?action=query&prop=extracts&exlimit=max&explaintext")
    WikipediaPageExtractResponse getPageExtractContent(@RequestParam("pageids") String pageIds);
}
