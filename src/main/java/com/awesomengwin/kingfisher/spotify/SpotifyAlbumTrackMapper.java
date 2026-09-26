package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.library.AlbumTrack;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = SpotifyArtistMapper.class)
public interface SpotifyAlbumTrackMapper
        extends SpotifyPageMapper<SpotifyAlbumTrack, AlbumTrack> {

    AlbumTrack map(SpotifyAlbumTrack source);
}
