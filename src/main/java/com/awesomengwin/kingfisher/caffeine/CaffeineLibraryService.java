package com.awesomengwin.kingfisher.caffeine;

import com.awesomengwin.kingfisher.library.*;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.time.Duration;

public class CaffeineLibraryService implements LibraryService {

    private final LibraryService wrappee;
    private final LoadingCache<UserSavedTracksKey, Page<UserSavedTrack>> userSavedTracksLoadingCache;
    private final LoadingCache<UserSavedAlbumsKey, Page<UserSavedAlbum>> userSavedAlbumsLoadingCache;
    private final LoadingCache<UserPlaylistsKey, Page<UserPlaylist>> userPlaylistsLoadingCache;
    private final LoadingCache<PlaylistTracksKey, Page<Track>> playlistTracksLoadingCache;
    private final LoadingCache<AlbumTracksKey, Page<AlbumTrack>> albumTracksLoadingCache;
    private final LoadingCache<TrackKey, Track> trackLoadingCache;

    public CaffeineLibraryService(LibraryService libraryService) {
        this.wrappee = libraryService;
        this.userSavedTracksLoadingCache = createCommonCaffeineLoadingCache(this::loadUserSavedTracks);
        this.userSavedAlbumsLoadingCache = createCommonCaffeineLoadingCache(this::loadUserSavedAlbums);
        this.userPlaylistsLoadingCache = createCommonCaffeineLoadingCache(this::loadUserPlaylists);
        this.playlistTracksLoadingCache = createCommonCaffeineLoadingCache(this::loadPlaylistTracks);
        this.albumTracksLoadingCache = createCommonCaffeineLoadingCache(this::loadAlbumTracks);
        this.trackLoadingCache = createCommonCaffeineLoadingCache(this::loadTrack);
    }

    @Override
    public Page<UserSavedTrack> getUserSavedTracks(String userId, Pageable p) {
        return userSavedTracksLoadingCache.get(new UserSavedTracksKey(userId, p.page(), p.size()));
    }

    @Override
    public Page<UserSavedAlbum> getUserSavedAlbums(String userId, Pageable p) {
        return userSavedAlbumsLoadingCache.get(new UserSavedAlbumsKey(userId, p.page(), p.size()));
    }

    @Override
    public Page<UserPlaylist> getUserPlaylists(String userId, Pageable p) {
        return userPlaylistsLoadingCache.get(new UserPlaylistsKey(userId, p.page(), p.size()));
    }

    @Override
    public Page<Track> getPlaylistTracks(String playlistId, String userId, Pageable p) {
        return playlistTracksLoadingCache.get(new PlaylistTracksKey(playlistId, userId, p.page(), p.size()));
    }

    @Override
    public Page<AlbumTrack> getAlbumTracks(String albumId, Pageable p) {
        return albumTracksLoadingCache.get(new AlbumTracksKey(albumId, p.page(), p.size()));
    }

    @Override
    public Track getTrack(String trackId) {
        return trackLoadingCache.get(new TrackKey(trackId));
    }

    private Page<UserSavedTrack> loadUserSavedTracks(UserSavedTracksKey key) {
        return wrappee.getUserSavedTracks(key.userId(), new Pageable(key.page(), key.size()));
    }

    private Page<UserSavedAlbum> loadUserSavedAlbums(UserSavedAlbumsKey key) {
        return wrappee.getUserSavedAlbums(key.userId(), new Pageable(key.page(), key.size()));
    }

    private Page<UserPlaylist> loadUserPlaylists(UserPlaylistsKey key) {
        return wrappee.getUserPlaylists(key.userId(), new Pageable(key.page(), key.size()));
    }

    private Page<Track> loadPlaylistTracks(PlaylistTracksKey key) {
        return wrappee.getPlaylistTracks(key.playlistId(), key.userId(), new Pageable(key.page(), key.size()));
    }

    private Page<AlbumTrack> loadAlbumTracks(AlbumTracksKey key) {
        return wrappee.getAlbumTracks(key.albumId(), new Pageable(key.page(), key.size()));
    }

    private Track loadTrack(TrackKey key) {
        return wrappee.getTrack(key.trackId());
    }

    private <K, V> LoadingCache<K, V> createCommonCaffeineLoadingCache(CacheLoader<K, V> loader) {
        return Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(Duration.ofMinutes(10))
                .build(loader);
    }
}
