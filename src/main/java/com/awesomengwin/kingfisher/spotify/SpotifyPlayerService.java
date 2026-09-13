package com.awesomengwin.kingfisher.spotify;

public interface SpotifyPlayerService {

    void startPlayback(String deviceId, String uri);

    void togglePlaybackShuffle(String deviceId, boolean state);

    void setRepeatMode(String deviceId, String state);
}
