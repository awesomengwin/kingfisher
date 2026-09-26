package com.awesomengwin.kingfisher.spotify;

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
    SpotifyPage<SpotifyUserSavedTrack> getUserSavedTracks(@RequestParam int limit, @RequestParam int offset);

    @GetExchange("/me/albums")
    @ClientRegistrationId("spotify-login")
    SpotifyPage<SpotifyUserSavedAlbum> getUserSavedAlbums(@RequestParam int limit, @RequestParam int offset);

    @GetExchange("/me/playlists")
    @ClientRegistrationId("spotify-login")
    SpotifyPage<SpotifyUserPlaylist> getUserPlaylists(@RequestParam int limit, @RequestParam int offset);

    @GetExchange("/playlists/{playlistId}/items")
    @ClientRegistrationId("spotify-login")
    SpotifyPage<SpotifyPlaylistTrack> getPlaylistTracks(@PathVariable String playlistId, @RequestParam int limit, @RequestParam int offset);

    @GetExchange("/albums/{albumId}/tracks")
    @ClientRegistrationId("spotify-login")
    SpotifyPage<SpotifyAlbumTrack> getAlbumTracks(@PathVariable String albumId, @RequestParam int limit, @RequestParam int offset);

    @GetExchange("/tracks/{trackId}")
    @ClientRegistrationId("spotify-service")
    SpotifyTrack getTrack(@PathVariable String trackId);

    @PutExchange("/me/player/play")
    @ClientRegistrationId("spotify-login")
    void startPlayback(@RequestParam("device_id") String deviceId, @RequestBody SpotifyStartPlaybackRequest request);

    @PutExchange("/me/player/shuffle")
    @ClientRegistrationId("spotify-login")
    void togglePlaybackShuffle(@RequestParam("device_id") String deviceId, @RequestParam boolean state);

    @PutExchange("/me/player/repeat")
    @ClientRegistrationId("spotify-login")
    void setRepeatMode(@RequestParam("device_id") String deviceId, @RequestParam String state);
}
