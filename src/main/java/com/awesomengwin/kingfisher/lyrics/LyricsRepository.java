package com.awesomengwin.kingfisher.lyrics;

import java.util.Optional;

public interface LyricsRepository {

    Optional<Lyrics> findById(String trackId);

    void save(Lyrics lyrics);

    boolean existsById(String trackId);
}
