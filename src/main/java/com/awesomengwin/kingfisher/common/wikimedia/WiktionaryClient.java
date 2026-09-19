package com.awesomengwin.kingfisher.common.wikimedia;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface WiktionaryClient {

    @GetExchange("/page/definition/{term}")
    TermDefinitionResponse getTermDefinition(@PathVariable String term);
}
