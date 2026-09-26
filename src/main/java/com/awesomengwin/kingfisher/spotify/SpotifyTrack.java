package com.awesomengwin.kingfisher.spotify;

import java.util.List;

public record SpotifyTrack(String uri, String name, SpotifyAlbum album, List<SpotifyArtist> artists) {
}
