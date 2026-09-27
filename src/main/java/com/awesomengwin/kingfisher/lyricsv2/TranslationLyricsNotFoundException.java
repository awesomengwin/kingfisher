package com.awesomengwin.kingfisher.lyricsv2;

public class TranslationLyricsNotFoundException extends RuntimeException {
    public TranslationLyricsNotFoundException(String trackId, String userId) {
        super("Translation lyrics with trackId %s and userId %s could not be found".formatted(trackId, userId));
    }
}
