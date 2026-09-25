package com.awesomengwin.kingfisher.spotifylegacy.client;

import org.springframework.security.oauth2.client.annotation.ClientRegistrationId;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetExchange("/tracks/{trackId}")
    @ClientRegistrationId("spotify-service")
    Track getTrack(@PathVariable String trackId);

    @GetExchange("/me/playlists")
    @ClientRegistrationId("spotify-login")
    SpotifyPage<PlaylistResponse> getUserPlaylists(@RequestParam int limit, @RequestParam int offset);

    @GetExchange("/playlists/{playlistId}/items")
    @ClientRegistrationId("spotify-login")
    SpotifyPage<PlaylistTrackResponse> getPlaylistTracks(@PathVariable String playlistId,
                                                         @RequestParam int limit, @RequestParam int offset);

    @PutExchange("/me/player/play")
    @ClientRegistrationId("spotify-login")
    void startPlayback(@RequestParam("device_id") String deviceId, @RequestBody StartPlaybackRequest request);

    @PutExchange("/me/player/shuffle")
    @ClientRegistrationId("spotify-login")
    void togglePlaybackShuffle(@RequestParam("device_id") String deviceId, @RequestParam boolean state);

    @PutExchange("/me/player/repeat")
    @ClientRegistrationId("spotify-login")
    void setRepeatMode(@RequestParam("device_id") String deviceId, @RequestParam String state);
}
