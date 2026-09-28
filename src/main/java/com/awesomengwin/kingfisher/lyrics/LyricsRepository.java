package com.awesomengwin.kingfisher.lyrics;

import java.util.Optional;

public interface LyricsRepository {

    Optional<Lyrics> findLyrics(String trackId);

    void saveLyrics(Lyrics lyrics);

}
