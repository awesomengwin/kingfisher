package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.library.Track;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SpotifyAlbumMapper.class, SpotifyArtistMapper.class})
public interface SpotifyTrackMapper {

    Track map(SpotifyTrack source);

    default Track map(SpotifyPlaylistTrack source) {
        return source == null ? null : map(source.item());
    }
}
