package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.spotify.valueobject.Page;
import com.awesomengwin.kingfisher.spotify.valueobject.SavedTrack;

public interface SpotifyService {

    Page<SavedTrack> getUserSavedTracks(String userId, int limit, int offset);
}
