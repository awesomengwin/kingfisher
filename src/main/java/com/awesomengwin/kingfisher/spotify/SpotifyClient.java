package com.awesomengwin.kingfisher.spotify;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange
public interface SpotifyClient {

    @GetExchange("/me/tracks")
    SpotifyPage<SpotifyUserSavedTrack> getUserSavedTracks(@RequestParam int limit, @RequestParam int offset);

    @GetExchange("/me/albums")
    SpotifyPage<SpotifyUserSavedAlbum> getUserSavedAlbums(@RequestParam int limit, @RequestParam int offset);

    @GetExchange("/me/playlists")
    SpotifyPage<SpotifyUserPlaylist> getUserPlaylists(@RequestParam int limit, @RequestParam int offset);

    @GetExchange("/playlists/{playlistId}/items")
    SpotifyPage<SpotifyPlaylistTrack> getPlaylistTracks(@PathVariable String playlistId, @RequestParam int limit, @RequestParam int offset);

    @GetExchange("/albums/{albumId}/tracks")
    SpotifyPage<SpotifyAlbumTrack> getAlbumTracks(@PathVariable String albumId, @RequestParam int limit, @RequestParam int offset);

    @GetExchange("/tracks/{trackId}")
    SpotifyTrack getTrack(@PathVariable String trackId);

    @PutExchange("/me/player/play")
    void startPlayback(@RequestParam("device_id") String deviceId, @RequestBody SpotifyStartPlaybackRequest request);

    @PutExchange("/me/player/shuffle")
    void togglePlaybackShuffle(@RequestParam("device_id") String deviceId, @RequestParam boolean state);

    @PutExchange("/me/player/repeat")
    void setRepeatMode(@RequestParam("device_id") String deviceId, @RequestParam String state);
}
