package com.awesomengwin.kingfisher.lyricsv2;

import java.util.Optional;

public interface LyricsRepository {

    Optional<Lyrics> findLyrics(String trackId);

    void saveLyrics(Lyrics lyrics);

}
