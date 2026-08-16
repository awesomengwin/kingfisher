package com.awesomengwin.kingfisher.spotify.impl;

import com.awesomengwin.kingfisher.spotify.client.SpotifyClient;
import com.awesomengwin.kingfisher.spotify.SpotifyPlayerService;
import com.awesomengwin.kingfisher.spotify.SpotifyService;
import com.awesomengwin.kingfisher.spotify.dto.Page;
import com.awesomengwin.kingfisher.spotify.dto.request.StartPlaybackRequest;
import com.awesomengwin.kingfisher.spotify.dto.response.SavedTrack;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CaffeineHttpServiceClientSpotifyService implements SpotifyService, SpotifyPlayerService {

    private final SpotifyClient spotifyClient;
    private final LoadingCache<SpotifyCacheKey, Page<SavedTrack>> userSavedTracksLoadingCache;

    public CaffeineHttpServiceClientSpotifyService(SpotifyClient spotifyClient) {
        this.spotifyClient = spotifyClient;
        this.userSavedTracksLoadingCache = createCommonCaffeineLoadingCache(this::loadUserSavedTracks);
    }

    @Override
    public Page<SavedTrack> getUserSavedTracks(String userId, int limit, int offset) {
        return userSavedTracksLoadingCache.get(
                new SpotifyCacheKey(userId, "user-saved-tracks", limit, offset));
    }

    @Override
    public void startPlayback(String deviceId, String uri) {
        spotifyClient.startPlayback(deviceId, new StartPlaybackRequest(uri));
    }

    private Page<SavedTrack> loadUserSavedTracks(SpotifyCacheKey key) {
        return spotifyClient.getUserSavedTracks(key.limit(), key.offset());
    }

    private <K, V> LoadingCache<K, V> createCommonCaffeineLoadingCache(CacheLoader<K, V> loader) {
        return Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(Duration.ofMinutes(10))
                .build(loader);
    }
}
