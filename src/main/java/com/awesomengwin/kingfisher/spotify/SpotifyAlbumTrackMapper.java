package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.library.AlbumTrack;
import org.mapstruct.Mapper;

@Mapper(uses = SpotifyArtistMapper.class)
public interface SpotifyAlbumTrackMapper
        extends SpotifyPageMapper<SpotifyAlbumTrack, AlbumTrack> {

    AlbumTrack map(SpotifyAlbumTrack source);
}
