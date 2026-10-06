package com.awesomengwin.kingfisher.lyrics.jpa;

import com.awesomengwin.kingfisher.lyrics.TranslationStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "translation_lyrics")
public class TranslationEntity extends AbstractAggregateRoot<TranslationEntity> {

    @EmbeddedId
    private Id id;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<Line> lines = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TranslationStatus status;

    private String failureReason;

    public TranslationEntity() {
    }

    public TranslationEntity(String trackId, String userId, List<Line> lines, TranslationStatus status, String failureReason) {
        this.id = new Id(trackId, userId);
        this.lines = new ArrayList<>(lines);
        this.status = status;
        this.failureReason = failureReason;
    }

    public void addDomainEvent(Object event) {
        registerEvent(event);
    }

    public Id getId() {
        return id;
    }

    public List<Line> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public TranslationStatus getStatus() {
        return status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    @Embeddable
    public record Id(String trackId, String userId) {
    }

    public record Line(Long startTimeMs, String translatedWords) {
    }
}
