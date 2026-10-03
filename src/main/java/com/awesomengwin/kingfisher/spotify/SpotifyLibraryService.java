package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.library.*;

import java.util.List;
import java.util.function.Function;

public class SpotifyLibraryService implements LibraryService {

    private final SpotifyClient client;

    public SpotifyLibraryService(SpotifyClient client) {
        this.client = client;
    }

    @Override
    public Page<UserSavedTrack> getUserSavedTracks(String userId, Pageable p) {
        SpotifyPage<SpotifyUserSavedTrack> page = client.getUserSavedTracks(p.limit(), p.offset());

        return toPage(page, sp -> new UserSavedTrack(sp.addedAt(), toTrack(sp.track())));
    }

    @Override
    public Page<UserSavedAlbum> getUserSavedAlbums(String userId, Pageable p) {
        SpotifyPage<SpotifyUserSavedAlbum> page = client.getUserSavedAlbums(p.limit(), p.offset());

        return toPage(page, sp -> new UserSavedAlbum(sp.addedAt(), toAlbum(sp.album())));
    }

    @Override
    public Page<UserPlaylist> getUserPlaylists(String userId, Pageable p) {
        SpotifyPage<SpotifyUserPlaylist> page = client.getUserPlaylists(p.limit(), p.offset());

        return toPage(page, sp -> new UserPlaylist(
                sp.id(), sp.name(), sp.owner().displayName(), sp.items().total()));
    }

    @Override
    public Page<Track> getPlaylistTracks(String playlistId, String userId, Pageable p) {
        SpotifyPage<SpotifyPlaylistTrack> page = client.getPlaylistTracks(playlistId, p.limit(), p.offset());

        return toPage(page, sp -> toTrack(sp.item()));
    }

    @Override
    public Page<AlbumTrack> getAlbumTracks(String albumId, Pageable p) {
        SpotifyPage<SpotifyAlbumTrack> page = client.getAlbumTracks(albumId, p.limit(), p.offset());

        return toPage(page, sp -> new AlbumTrack(sp.name(), sp.artists().stream()
                .map(this::toArtist)
                .toList()));
    }

    @Override
    public Track getTrack(String trackId) {
        return toTrack(client.getTrack(trackId));
    }

    private Track toTrack(SpotifyTrack spTrack) {
        return new Track(
                spTrack.uri(),
                spTrack.name(),
                toAlbum(spTrack.album()),
                spTrack.artists().stream()
                        .map(this::toArtist)
                        .toList());
    }

    private Album toAlbum(SpotifyAlbum spAlbum) {
        return new Album(spAlbum.name(), spAlbum.images().stream()
                .map(spImg -> new Image(spImg.url()))
                .toList());
    }

    private Artist toArtist(SpotifyArtist spArtist) {
        return new Artist(spArtist.name());
    }

    private <S, T> Page<T> toPage(SpotifyPage<S> spPage, Function<S, T> itemsMapper) {
        int limit = spPage.limit();

        int page = limit == 0 ? 0 : spPage.offset() / limit;
        int totalPages = limit == 0 ? 0 : (spPage.total() + limit - 1) / limit;

        List<T> items = spPage.items().stream()
                .map(itemsMapper)
                .toList();

        return new Page<>(page, limit, totalPages, spPage.total(), items);
    }
}
