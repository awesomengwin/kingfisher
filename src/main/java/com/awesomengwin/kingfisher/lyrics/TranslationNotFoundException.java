package com.awesomengwin.kingfisher.lyrics;

public class TranslationNotFoundException extends RuntimeException {
    public TranslationNotFoundException(String trackId, String userId) {
        super("Translation with trackId %s and userId %s could not be found".formatted(trackId, userId));
    }
}
