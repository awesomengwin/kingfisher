package com.awesomengwin.kingfisher.spotify.client;

import org.springframework.security.oauth2.client.annotation.ClientRegistrationId;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange
public interface SpotifyClient {

    @GetExchange("/me/tracks")
    @ClientRegistrationId("spotify-login")
    SpotifyPage<SavedTrackResponse> getUserSavedTracks(@RequestParam int limit, @RequestParam int offset);

    @PutExchange("/me/player/play")
    @ClientRegistrationId("spotify-login")
    void startPlayback(@RequestParam("device_id") String deviceId, @RequestBody StartPlaybackRequest request);
}
