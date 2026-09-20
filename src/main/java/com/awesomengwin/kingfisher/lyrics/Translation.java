package com.awesomengwin.kingfisher.lyrics;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "translation")
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trackId;

    private String userId;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<TranslatedLine> lines = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TranslateStatus translateStatus;

    public Translation() {
    }

    public Translation(String trackId, String userId, List<TranslatedLine> lines) {
        this.trackId = trackId;
        this.userId = userId;
        this.lines = lines;

        updateTranslateStatus();
    }

    public void updateLines(List<TranslatedLine> lines) {
        this.lines = lines;

        updateTranslateStatus();
    }

    private void updateTranslateStatus() {
        boolean isPartialTranslated = this.lines.stream()
                .anyMatch(line -> StringUtils.hasText(line.translatedWords()));

        if (!isPartialTranslated) {
            this.translateStatus = TranslateStatus.NONE;
            return;
        }

        boolean isCompletedTranslated = this.lines.stream()
                .allMatch(line -> StringUtils.hasText(line.translatedWords()));

        if (isCompletedTranslated) {
            this.translateStatus = TranslateStatus.COMPLETED;
        } else {
            this.translateStatus = TranslateStatus.PARTIAL;
        }
    }

    public Long getId() {
        return id;
    }

    public String getTrackId() {
        return trackId;
    }

    public String getUserId() {
        return userId;
    }

    public List<TranslatedLine> getLines() {
        return lines;
    }

    public TranslateStatus getTranslateStatus() {
        return translateStatus;
    }
}
