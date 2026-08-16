package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.spotify.client.SpotifyPage;
import com.awesomengwin.kingfisher.spotify.client.SavedTrackResponse;

public interface SpotifyService {

    SpotifyPage<SavedTrackResponse> getUserSavedTracks(String userId, int limit, int offset);
}
