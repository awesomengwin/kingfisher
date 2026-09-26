package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.library.Image;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpotifyImageMapper {

    Image map(SpotifyImage source);
}
