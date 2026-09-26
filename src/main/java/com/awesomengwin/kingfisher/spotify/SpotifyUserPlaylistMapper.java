package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.library.UserPlaylist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SpotifyUserPlaylistMapper
        extends SpotifyPageMapper<SpotifyUserPlaylist, UserPlaylist> {

    @Mapping(target = "ownerName", source = "owner.displayName")
    @Mapping(target = "totalItems", source = "items.total")
    UserPlaylist map(SpotifyUserPlaylist source);
}
