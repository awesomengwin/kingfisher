package com.awesomengwin.kingfisher.lyrics.spotifylyrics;

public record SpotifyLyricsLine(
        Long startTimeMs,
        String words,
        Long endTimeMs
) {
}
