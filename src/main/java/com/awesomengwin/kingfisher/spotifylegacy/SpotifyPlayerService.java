package com.awesomengwin.kingfisher.spotifylegacy;

public interface SpotifyPlayerService {

    void startPlayback(String userId, String deviceId, String contextUri, String uri);

    void togglePlaybackShuffle(String deviceId, boolean state);

    void setRepeatMode(String deviceId, String state);
}
