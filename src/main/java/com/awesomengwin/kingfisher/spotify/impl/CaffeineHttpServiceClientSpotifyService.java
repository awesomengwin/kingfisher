package com.awesomengwin.kingfisher.spotify.impl;

import com.awesomengwin.kingfisher.spotify.client.*;
import com.awesomengwin.kingfisher.spotify.SpotifyPlayerService;
import com.awesomengwin.kingfisher.spotify.SpotifyService;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CaffeineHttpServiceClientSpotifyService implements SpotifyService, SpotifyPlayerService {

    private final SpotifyClient spotifyClient;
    private final LoadingCache<SpotifyCacheKey, SpotifyPage<SavedTrackResponse>> userSavedTracksLoadingCache;
    private final LoadingCache<SpotifyCacheKey, SpotifyPage<PlaylistResponse>> userPlaylistsLoadingCache;
    private final LoadingCache<SpotifyCacheKey, SpotifyPage<PlaylistTrackResponse>> playlistTracksLoadingCache;

    public CaffeineHttpServiceClientSpotifyService(SpotifyClient spotifyClient) {
        this.spotifyClient = spotifyClient;
        this.userSavedTracksLoadingCache = createCommonCaffeineLoadingCache(this::loadUserSavedTracks);
        this.userPlaylistsLoadingCache = createCommonCaffeineLoadingCache(this::loadUserPlaylists);
        this.playlistTracksLoadingCache = createCommonCaffeineLoadingCache(this::loadPlaylistTracks);
    }

    @Override
    public SpotifyPage<SavedTrackResponse> getUserSavedTracks(String userId, int limit, int offset) {
        return userSavedTracksLoadingCache.get(
                new SpotifyCacheKey(userId, "user-saved-tracks", limit, offset));
    }

    @Override
    public SpotifyPage<PlaylistResponse> getUserPlaylists(String userId, int limit, int offset) {
        return userPlaylistsLoadingCache.get(
                new SpotifyCacheKey(userId, "user-playlists", limit, offset));
    }

    @Override
    public SpotifyPage<PlaylistTrackResponse> getPlaylistTracks(String userId, String playlistId, int limit, int offset) {
        return playlistTracksLoadingCache.get(
                new SpotifyCacheKey(userId, "playlists:%s".formatted(playlistId), playlistId, limit, offset));
    }

    @Override
    public void startPlayback(String deviceId, String uri) {
        spotifyClient.startPlayback(deviceId, new StartPlaybackRequest(uri));
    }

    @Override
    public void togglePlaybackShuffle(String deviceId, boolean state) {
        spotifyClient.togglePlaybackShuffle(deviceId, state);
    }

    @Override
    public void setRepeatMode(String deviceId, String state) {
        spotifyClient.setRepeatMode(deviceId, state);
    }

    private SpotifyPage<SavedTrackResponse> loadUserSavedTracks(SpotifyCacheKey key) {
        return spotifyClient.getUserSavedTracks(key.limit(), key.offset());
    }

    private SpotifyPage<PlaylistResponse> loadUserPlaylists(SpotifyCacheKey key) {
        return spotifyClient.getUserPlaylists(key.limit(), key.offset());
    }

    private SpotifyPage<PlaylistTrackResponse> loadPlaylistTracks(SpotifyCacheKey key) {
        return spotifyClient.getPlaylistTracks(key.resourceId(), key.limit(), key.offset());
    }

    private <K, V> LoadingCache<K, V> createCommonCaffeineLoadingCache(CacheLoader<K, V> loader) {
        return Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(Duration.ofMinutes(10))
                .build(loader);
    }
}
