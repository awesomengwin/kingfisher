package com.awesomengwin.kingfisher.lyrics;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lyrics")
public class Lyrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trackId;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<LyricsLine> lines = new ArrayList<>();

    public Lyrics() {
    }

    public Lyrics(String trackId, List<LyricsLine> lines) {
        this.trackId = trackId;
        this.lines = lines;
    }

    public Long getId() {
        return id;
    }

    public String getTrackId() {
        return trackId;
    }

    public List<LyricsLine> getLines() {
        return lines;
    }
}
