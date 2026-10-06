package com.awesomengwin.kingfisher.lyrics.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lyrics")
public class LyricsEntity {

    @Id
    private String trackId;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<Line> lines = new ArrayList<>();

    public LyricsEntity() {
    }

    public LyricsEntity(String trackId, List<Line> lines) {
        this.trackId = trackId;
        this.lines = lines;
    }

    public String getTrackId() {
        return trackId;
    }

    public List<Line> getLines() {
        return lines;
    }

    public record Line(Long startTimeMs, String words, Long endTimeMs) {
    }
}
