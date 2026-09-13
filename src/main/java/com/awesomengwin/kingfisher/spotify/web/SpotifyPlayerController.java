package com.awesomengwin.kingfisher.spotify.web;

import com.awesomengwin.kingfisher.spotify.SpotifyPlayerService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    public void startPlayback(@AuthenticationPrincipal OAuth2User currentUser,
                              @RequestParam String deviceId,
                              @RequestParam(required = false) String contextUri, @RequestParam String uri) {
        spotifyPlayerService.startPlayback(currentUser.getName(), deviceId, contextUri, uri);
    }

    @PutMapping("/player/shuffle")
    public void togglePlaybackShuffle(@RequestParam String deviceId, @RequestParam boolean state) {
        spotifyPlayerService.togglePlaybackShuffle(deviceId, state);
    }

    @PutMapping("/player/repeat")
    public void setRepeatMode(@RequestParam String deviceId, @RequestParam String state) {
        spotifyPlayerService.setRepeatMode(deviceId, state);
    }
}
