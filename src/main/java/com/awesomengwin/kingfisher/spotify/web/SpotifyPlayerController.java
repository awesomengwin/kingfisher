package com.awesomengwin.kingfisher.spotify.web;

import com.awesomengwin.kingfisher.spotify.SpotifyPlayerService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spotify")
public class SpotifyPlayerController {

    private final SpotifyPlayerService spotifyPlayerService;

    public SpotifyPlayerController(SpotifyPlayerService spotifyPlayerService) {
        this.spotifyPlayerService = spotifyPlayerService;
    }

    @PostMapping("/token")
    public String getSpotifyOAuth2Token(@RegisteredOAuth2AuthorizedClient("spotify-login") OAuth2AuthorizedClient authorizedClient) {
        return authorizedClient.getAccessToken().getTokenValue();
    }

    @PutMapping("/player/play")
    public void startPlayback(@RequestParam String deviceId, @RequestParam String uri) {
        spotifyPlayerService.startPlayback(deviceId, uri);
    }
}
