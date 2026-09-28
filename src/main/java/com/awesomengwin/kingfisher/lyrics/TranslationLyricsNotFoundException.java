package com.awesomengwin.kingfisher.lyrics;

public class TranslationLyricsNotFoundException extends RuntimeException {
    public TranslationLyricsNotFoundException(String trackId, String userId) {
        super("Translation lyrics with trackId %s and userId %s could not be found".formatted(trackId, userId));
    }
}
