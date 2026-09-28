package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.player.PlayerService;
import com.awesomengwin.kingfisher.player.RepeatMode;
import org.springframework.stereotype.Service;

@Service
public class SpotifyPlayerService implements PlayerService {

    private final SpotifyClient client;

    public SpotifyPlayerService(SpotifyClient client) {
        this.client = client;
    }

    @Override
    public void play(String userId, String deviceId, String contextUri, String uri) {
        if (contextUri == null || contextUri.isBlank()) {
            client.startPlayback(deviceId,
                    new SpotifyStartPlaybackRequest("spotify:user:%s:collection".formatted(userId), uri));
            return;
        }

        client.startPlayback(deviceId, new SpotifyStartPlaybackRequest(contextUri, uri));
    }

    @Override
    public void shuffle(String deviceId, boolean enabled) {
        client.togglePlaybackShuffle(deviceId, enabled);
    }

    @Override
    public void repeat(String deviceId, RepeatMode mode) {
        String state;
        switch (mode) {
            case OFF -> state = "off";
            case CONTEXT -> state = "context";
            case TRACK -> state = "track";
            default -> throw new IllegalStateException("Unsupported repeat mode %s".formatted(mode.name()));
        }

        client.setRepeatMode(deviceId, state);
    }
}
