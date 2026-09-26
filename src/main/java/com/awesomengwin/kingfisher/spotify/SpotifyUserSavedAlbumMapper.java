package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.library.UserSavedAlbum;
import org.mapstruct.Mapper;

@Mapper(uses = SpotifyAlbumMapper.class)
public interface SpotifyUserSavedAlbumMapper
        extends SpotifyPageMapper<SpotifyUserSavedAlbum, UserSavedAlbum> {

    UserSavedAlbum map(SpotifyUserSavedAlbum source);
}
