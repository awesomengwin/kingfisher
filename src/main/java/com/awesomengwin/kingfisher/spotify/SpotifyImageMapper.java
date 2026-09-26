package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.library.Image;
import org.mapstruct.Mapper;

@Mapper
public interface SpotifyImageMapper {

    Image map(SpotifyImage source);
}
