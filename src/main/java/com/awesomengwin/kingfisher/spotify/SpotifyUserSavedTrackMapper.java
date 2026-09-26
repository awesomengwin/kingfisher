package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.library.UserSavedTrack;
import org.mapstruct.Mapper;

@Mapper(uses = SpotifyTrackMapper.class)
public interface SpotifyUserSavedTrackMapper
        extends SpotifyPageMapper<SpotifyUserSavedTrack, UserSavedTrack> {

    UserSavedTrack map(SpotifyUserSavedTrack source);
}
