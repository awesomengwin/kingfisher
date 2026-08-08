package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.spotify.valueobject.Page;
import com.awesomengwin.kingfisher.spotify.valueobject.SavedTrack;
import org.springframework.security.oauth2.client.annotation.ClientRegistrationId;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface SpotifyApi {

    @GetExchange("/me/tracks")
    @ClientRegistrationId("spotify-login")
    Page<SavedTrack> getUserSavedTracks(@RequestParam int limit, @RequestParam int offset);
}
