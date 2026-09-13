package com.awesomengwin.kingfisher.lyrics.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface LyricsClient {

    @GetExchange
    LyricsApiResponse getLyrics(@RequestParam String trackId);
}
