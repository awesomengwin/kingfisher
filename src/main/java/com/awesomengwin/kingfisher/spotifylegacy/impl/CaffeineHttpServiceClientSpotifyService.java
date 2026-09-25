package com.awesomengwin.kingfisher.spotifylegacy.impl;

import com.awesomengwin.kingfisher.spotifylegacy.client.*;
import com.awesomengwin.kingfisher.spotifylegacy.SpotifyPlayerService;
import com.awesomengwin.kingfisher.spotifylegacy.SpotifyService;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;

@Service
public class CaffeineHttpServiceClientSpotifyService implements SpotifyService, SpotifyPlayerService {

    private final SpotifyClient spotifyClient;
    private final LoadingCache<SpotifyCacheKey, SpotifyPage<SavedTrackResponse>> userSavedTracksLoadingCache;
    private final LoadingCache<SpotifyCacheKey, SpotifyPage<PlaylistResponse>> userPlaylistsLoadingCache;
    private final LoadingCache<SpotifyCacheKey, SpotifyPage<PlaylistTrackResponse>> playlistTracksLoadingCache;
    private final LoadingCache<SpotifyCacheKey, Track> trackLoadingCache;

    public CaffeineHttpServiceClientSpotifyService(SpotifyClient spotifyClient) {
        this.spotifyClient = spotifyClient;
        this.userSavedTracksLoadingCache = createCommonCaffeineLoadingCache(this::loadUserSavedTracks);
        this.userPlaylistsLoadingCache = createCommonCaffeineLoadingCache(this::loadUserPlaylists);
        this.playlistTracksLoadingCache = createCommonCaffeineLoadingCache(this::loadPlaylistTracks);
        this.trackLoadingCache = createCommonCaffeineLoadingCache(this::loadTrack);
    }

    @Override
    public SpotifyPage<SavedTrackResponse> getUserSavedTracks(String userId, int limit, int offset) {
        return userSavedTracksLoadingCache.get(
                new SpotifyCacheKey(userId, "user-saved-tracks", limit, offset));
    }

    @Override
    public Track getTrack(String trackId) {
        return trackLoadingCache.get(new SpotifyCacheKey("track", trackId));
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
    public void startPlayback(String userId, String deviceId, String contextUri, String uri) {
        if (StringUtils.hasText(contextUri)) {
            spotifyClient.startPlayback(deviceId, new StartPlaybackRequest(contextUri, uri));
            return;
        }
        spotifyClient.startPlayback(deviceId,
                new StartPlaybackRequest("spotify:user:%s:collection".formatted(userId), uri));
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

    private Track loadTrack(SpotifyCacheKey key) {
        return spotifyClient.getTrack(key.resourceId());
    }

    private <K, V> LoadingCache<K, V> createCommonCaffeineLoadingCache(CacheLoader<K, V> loader) {
        return Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(Duration.ofMinutes(10))
                .build(loader);
    }
}
