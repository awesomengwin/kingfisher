package com.awesomengwin.kingfisher.lyrics.jpa;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "translation_lyrics")
public class TranslationEntity {

    @EmbeddedId
    private Id id;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<Line> lines = new ArrayList<>();

    public TranslationEntity() {
    }

    public TranslationEntity(String trackId, String userId, List<Line> lines) {
        this.id = new Id(trackId, userId);
        this.lines = lines;
    }

    public Id getId() {
        return id;
    }

    public List<Line> getLines() {
        return lines;
    }

    @Embeddable
    public record Id(String trackId, String userId) {
    }

    public record Line(Long startTimeMs, String translatedWords) {
    }
}
