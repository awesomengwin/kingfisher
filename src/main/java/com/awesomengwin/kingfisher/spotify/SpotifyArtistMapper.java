package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.library.Artist;
import org.mapstruct.Mapper;

@Mapper
public interface SpotifyArtistMapper {

    Artist map(SpotifyArtist source);
}
