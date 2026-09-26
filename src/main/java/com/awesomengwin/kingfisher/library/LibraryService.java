package com.awesomengwin.kingfisher.library;

public interface LibraryService {

    Page<UserSavedTrack> getUserSavedTracks(String userId, Pageable p);

    Page<UserSavedAlbum> getUserSavedAlbums(String userId, Pageable p);

    Page<UserPlaylist> getUserPlaylists(String userId, Pageable p);

    Page<Track> getPlaylistTracks(String playlistId, String userId, Pageable p);

    Page<AlbumTrack> getAlbumTracks(String albumId, Pageable p);

    Track getTrack(String trackId);

}
