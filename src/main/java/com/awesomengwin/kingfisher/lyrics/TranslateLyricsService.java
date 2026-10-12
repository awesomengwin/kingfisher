package com.awesomengwin.kingfisher.lyrics;

public interface TranslateLyricsService {

    TranslateLyricsResponse translate(Lyrics lyrics, String userId, TrackMetadata trackMetadata);

}
