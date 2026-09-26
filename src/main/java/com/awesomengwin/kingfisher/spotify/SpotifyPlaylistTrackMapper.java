package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.library.Track;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = SpotifyTrackMapper.class)
public interface SpotifyPlaylistTrackMapper
        extends SpotifyPageMapper<SpotifyPlaylistTrack, Track> {
}
