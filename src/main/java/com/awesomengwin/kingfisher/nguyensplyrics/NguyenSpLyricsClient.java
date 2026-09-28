package com.awesomengwin.kingfisher.nguyensplyrics;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface NguyenSpLyricsClient {

    @GetExchange
    NguyenSpLyricsLyrics getLyrics(@RequestParam String trackId);

}
