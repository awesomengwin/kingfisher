package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.library.*;

public class SpotifyLibraryService implements LibraryService {

    private final SpotifyClient client;
    private final SpotifyUserSavedTrackMapper spotifyUserSavedTrackMapper;
    private final SpotifyUserSavedAlbumMapper spotifyUserSavedAlbumMapper;
    private final SpotifyUserPlaylistMapper spotifyUserPlaylistMapper;
    private final SpotifyPlaylistTrackMapper spotifyPlaylistTrackMapper;
    private final SpotifyAlbumTrackMapper spotifyAlbumTrackMapper;
    private final SpotifyTrackMapper spotifyTrackMapper;

    public SpotifyLibraryService(SpotifyClient client, SpotifyUserSavedTrackMapper spotifyUserSavedTrackMapper, SpotifyUserSavedAlbumMapper spotifyUserSavedAlbumMapper, SpotifyUserPlaylistMapper spotifyUserPlaylistMapper, SpotifyPlaylistTrackMapper spotifyPlaylistTrackMapper, SpotifyAlbumTrackMapper spotifyAlbumTrackMapper, SpotifyTrackMapper spotifyTrackMapper) {
        this.client = client;
        this.spotifyUserSavedTrackMapper = spotifyUserSavedTrackMapper;
        this.spotifyUserSavedAlbumMapper = spotifyUserSavedAlbumMapper;
        this.spotifyUserPlaylistMapper = spotifyUserPlaylistMapper;
        this.spotifyPlaylistTrackMapper = spotifyPlaylistTrackMapper;
        this.spotifyAlbumTrackMapper = spotifyAlbumTrackMapper;
        this.spotifyTrackMapper = spotifyTrackMapper;
    }

    @Override
    public Page<UserSavedTrack> getUserSavedTracks(String userId, Pageable p) {
        return spotifyUserSavedTrackMapper.toPage(client.getUserSavedTracks(p.limit(), p.offset()));
    }

    @Override
    public Page<UserSavedAlbum> getUserSavedAlbums(String userId, Pageable p) {
        return spotifyUserSavedAlbumMapper.toPage(client.getUserSavedAlbums(p.limit(), p.offset()));
    }

    @Override
    public Page<UserPlaylist> getUserPlaylists(String userId, Pageable p) {
        return spotifyUserPlaylistMapper.toPage(client.getUserPlaylists(p.limit(), p.offset()));
    }

    @Override
    public Page<Track> getPlaylistTracks(String playlistId, String userId, Pageable p) {
        return spotifyPlaylistTrackMapper.toPage(client.getPlaylistTracks(playlistId, p.limit(), p.offset()));
    }

    @Override
    public Page<AlbumTrack> getAlbumTracks(String albumId, Pageable p) {
        return spotifyAlbumTrackMapper.toPage(client.getAlbumTracks(albumId, p.limit(), p.offset()));
    }

    @Override
    public Track getTrack(String trackId) {
        return spotifyTrackMapper.map(client.getTrack(trackId));
    }
}
