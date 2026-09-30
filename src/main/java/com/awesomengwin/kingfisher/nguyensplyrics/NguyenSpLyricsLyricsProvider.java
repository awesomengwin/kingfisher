package com.awesomengwin.kingfisher.nguyensplyrics;

import com.awesomengwin.kingfisher.lyrics.Lyrics;
import com.awesomengwin.kingfisher.lyrics.LyricsLine;
import com.awesomengwin.kingfisher.lyrics.LyricsProvider;
import org.springframework.stereotype.Service;

@Service
public class NguyenSpLyricsLyricsProvider implements LyricsProvider {

    private final NguyenSpLyricsClient client;

    public NguyenSpLyricsLyricsProvider(NguyenSpLyricsClient client) {
        this.client = client;
    }

    @Override
    public Lyrics getLyrics(String trackId) {
        NguyenSpLyricsLyrics ngSpLyrics = client.getLyrics(trackId);

        return new Lyrics(trackId, ngSpLyrics.lines().stream()
                .map(l -> new LyricsLine(l.startTimeMs(), l.words(), l.endTimeMs()))
                .toList());
    }
}
