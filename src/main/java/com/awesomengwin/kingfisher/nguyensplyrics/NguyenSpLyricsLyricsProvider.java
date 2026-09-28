package com.awesomengwin.kingfisher.nguyensplyrics;

import com.awesomengwin.kingfisher.lyrics.Lyrics;
import com.awesomengwin.kingfisher.lyrics.LyricsProvider;
import org.springframework.stereotype.Service;

@Service
public class NguyenSpLyricsLyricsProvider implements LyricsProvider {

    private final NguyenSpLyricsClient client;
    private final NguyenSpLyricsLyricsMapper mapper;

    public NguyenSpLyricsLyricsProvider(NguyenSpLyricsClient client, NguyenSpLyricsLyricsMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public Lyrics getLyrics(String trackId) {
        return mapper.toLyrics(client.getLyrics(trackId));
    }
}
