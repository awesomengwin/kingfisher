package com.awesomengwin.kingfisher.lyrics;

public class LyricsNotFoundException extends RuntimeException {
    public LyricsNotFoundException(String trackId) {
        super("Lyrics with trackId %s could not be found".formatted(trackId));
    }
}
