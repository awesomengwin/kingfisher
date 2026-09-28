package com.awesomengwin.kingfisher.lyricsv2.infrastructure;

import com.awesomengwin.kingfisher.lyricsv2.TranslationLyricsStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "translation_lyrics")
public class TranslationLyricsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trackId;
    private String userId;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<TranslationLyricsLineValueObject> lines = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TranslationLyricsStatus translateStatus;

    public TranslationLyricsEntity() {
    }

    public TranslationLyricsEntity(String trackId, String userId, List<TranslationLyricsLineValueObject> lines) {
        this.trackId = trackId;
        this.userId = userId;
        this.lines = lines;

        updateTranslateStatus();
    }

    private void updateTranslateStatus() {
        boolean isPartialTranslated = this.lines.stream()
                .anyMatch(line -> StringUtils.hasText(line.translatedWords()));

        if (!isPartialTranslated) {
            this.translateStatus = TranslationLyricsStatus.NONE;
            return;
        }

        boolean isCompletedTranslated = this.lines.stream()
                .allMatch(line -> StringUtils.hasText(line.translatedWords()));

        if (isCompletedTranslated) {
            this.translateStatus = TranslationLyricsStatus.COMPLETED;
        } else {
            this.translateStatus = TranslationLyricsStatus.PARTIAL;
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

    public List<TranslationLyricsLineValueObject> getLines() {
        return lines;
    }

    public TranslationLyricsStatus getTranslateStatus() {
        return translateStatus;
    }
}
