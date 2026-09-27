package com.awesomengwin.kingfisher.lyricsv2;

public class LyricsNotFoundException extends RuntimeException {
    public LyricsNotFoundException(String trackId) {
        super("Lyrics with trackId %s could not be found".formatted(trackId));
    }
}
