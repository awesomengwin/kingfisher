package com.awesomengwin.kingfisher.lyrics.spotifylyrics;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface SpotifyLyricsClient {

    @GetExchange
    SpotifyLyrics getLyrics(@RequestParam String trackId);
}
