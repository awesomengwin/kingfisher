package com.awesomengwin.kingfisher.lyrics;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Testcontainers
class LyricsRepositoryTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres");

    @Autowired
    private LyricsRepository lyricsRepository;

    @Test
    void shouldOkWhenSaveAndFindByTrackId() {
        String trackId = "1SDiiE3v2z89VxC3aVRKHQ";

        List<LyricsLine> lines = new ArrayList<>();
        lines.add(new LyricsLine(1000L, "First", 2000L));
        lines.add(new LyricsLine(2000L, "Second", 3000L));
        lines.add(new LyricsLine(3000L, "Third", 4000L));

        Lyrics lyrics = new Lyrics(trackId, lines);

        lyricsRepository.save(lyrics);

        Lyrics savedLyrics = lyricsRepository.findByTrackId(trackId).orElseThrow();

        assertNotNull(savedLyrics);

        assertNotNull(savedLyrics.getId());

        assertEquals(trackId, savedLyrics.getTrackId());

        assertThat(savedLyrics.getLines())
                .extracting(LyricsLine::startTimeMs)
                .containsExactly(1000L, 2000L, 3000L);

        assertThat(savedLyrics.getLines())
                .extracting(LyricsLine::words)
                .containsExactly("First", "Second", "Third");

        assertThat(savedLyrics.getLines())
                .extracting(LyricsLine::endTimeMs)
                .containsExactly(2000L, 3000L, 4000L);
    }
}
