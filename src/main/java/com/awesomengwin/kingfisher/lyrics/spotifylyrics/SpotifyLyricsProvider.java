package com.awesomengwin.kingfisher.lyrics.spotifylyrics;

import com.awesomengwin.kingfisher.lyrics.Lyrics;
import com.awesomengwin.kingfisher.lyrics.LyricsLine;
import com.awesomengwin.kingfisher.lyrics.LyricsProvider;
import org.springframework.stereotype.Service;

@Service
public class SpotifyLyricsProvider implements LyricsProvider {

    private final SpotifyLyricsClient client;

    public SpotifyLyricsProvider(SpotifyLyricsClient client) {
        this.client = client;
    }

    @Override
    public Lyrics getLyrics(String trackId) {
        SpotifyLyrics lyrics = client.getLyrics(trackId);

        if (lyrics.lines() == null) {
            throw new IllegalStateException("Spotify lyrics lines must not be null");
        }

        return new Lyrics(trackId, lyrics.lines().stream()
                .map(l -> new LyricsLine(l.startTimeMs(), l.words(), l.endTimeMs()))
                .toList());
    }
}
