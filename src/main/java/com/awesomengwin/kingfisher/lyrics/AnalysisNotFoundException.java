package com.awesomengwin.kingfisher.lyrics;

public class AnalysisNotFoundException extends RuntimeException {
    public AnalysisNotFoundException(String trackId, String userId) {
        super("Analysis with trackId %s and userId %s could not be found".formatted(trackId, userId));
    }
}
