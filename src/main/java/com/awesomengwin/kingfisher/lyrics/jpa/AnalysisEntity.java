package com.awesomengwin.kingfisher.lyrics.jpa;

import com.awesomengwin.kingfisher.lyrics.AnalysisStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "analysis_lyrics")
public class AnalysisEntity extends AbstractAggregateRoot<AnalysisEntity> {

    @EmbeddedId
    private Id id;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<Annotation> annotations = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private AnalysisStatus status;

    private String failureReason;

    public AnalysisEntity() {
    }

    public AnalysisEntity(String trackId, String userId, List<Annotation> annotations, AnalysisStatus status, String failureReason) {
        this.id = new Id(trackId, userId);
        this.annotations = new ArrayList<>(annotations);
        this.status = status;
        this.failureReason = failureReason;
    }

    public void addDomainEvent(Object event) {
        registerEvent(event);
    }

    public Id getId() {
        return id;
    }

    public List<Annotation> getAnnotations() {
        return Collections.unmodifiableList(annotations);
    }

    public AnalysisStatus getStatus() {
        return status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    @Embeddable
    public record Id(String trackId, String userId) {
    }

    public record Annotation(
            Long fromTimeMs,
            Long toTimeMs,
            String body
    ) {
    }
}
