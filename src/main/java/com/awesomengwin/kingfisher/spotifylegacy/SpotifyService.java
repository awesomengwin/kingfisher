package com.awesomengwin.kingfisher.spotifylegacy;

import com.awesomengwin.kingfisher.spotifylegacy.client.*;

public interface SpotifyService {

    SpotifyPage<SavedTrackResponse> getUserSavedTracks(String userId, int limit, int offset);

    SpotifyPage<PlaylistResponse> getUserPlaylists(String userId, int limit, int offset);

    SpotifyPage<PlaylistTrackResponse> getPlaylistTracks(String userId, String playlistId, int limit, int offset);

    Track getTrack(String trackId);
}
