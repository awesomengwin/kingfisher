package com.awesomengwin.kingfisher.player;

public interface PlayerService {

    void play(String userId, String deviceId, String contextUri, String uri);

    void shuffle(String deviceId, boolean enabled);

    void repeat(String deviceId, RepeatMode mode);

}
