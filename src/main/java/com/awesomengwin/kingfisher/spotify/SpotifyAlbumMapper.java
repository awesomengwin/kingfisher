package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.library.Album;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = SpotifyImageMapper.class)
public interface SpotifyAlbumMapper {

    Album map(SpotifyAlbum source);
}
