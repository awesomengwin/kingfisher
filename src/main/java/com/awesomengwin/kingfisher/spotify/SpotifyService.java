package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.spotify.dto.Page;
import com.awesomengwin.kingfisher.spotify.dto.response.SavedTrack;

public interface SpotifyService {

    Page<SavedTrack> getUserSavedTracks(String userId, int limit, int offset);
}
