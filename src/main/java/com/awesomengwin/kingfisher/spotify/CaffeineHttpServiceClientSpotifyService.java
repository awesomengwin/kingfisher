package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.spotify.dto.Page;
import com.awesomengwin.kingfisher.spotify.dto.response.SavedTrack;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CaffeineHttpServiceClientSpotifyService implements SpotifyService {

    private final SpotifyApi spotifyApi;
    private final LoadingCache<String, Page<SavedTrack>> userSavedTracksLoadingCache;

    public CaffeineHttpServiceClientSpotifyService(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
        this.userSavedTracksLoadingCache = Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(Duration.ofMinutes(10))
                .build(this::loadUserSavedTracks);
    }

    @Override
    public Page<SavedTrack> getUserSavedTracks(String userId, int limit, int offset) {
        return userSavedTracksLoadingCache.get(generateSimpleKey(userId, limit, offset));
    }

    private Page<SavedTrack> loadUserSavedTracks(String key) {
        String[] parts = key.split(":");
        int limit = Integer.parseInt(parts[1]);
        int offset = Integer.parseInt(parts[2]);

        return spotifyApi.getUserSavedTracks(limit, offset);
    }

    private String generateSimpleKey(String userId, int limit, int offset) {
        return userId + ":" + limit + ":" + offset;
    }
}
