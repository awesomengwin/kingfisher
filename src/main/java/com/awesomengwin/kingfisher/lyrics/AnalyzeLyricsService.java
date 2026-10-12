package com.awesomengwin.kingfisher.lyrics;

public interface AnalyzeLyricsService {

    AnalyzeLyricsResponse analyze(Lyrics lyrics, String userId, TrackMetadata trackMetadata);

}
