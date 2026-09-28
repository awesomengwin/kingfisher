package com.awesomengwin.kingfisher.lyrics.infrastructure;

import com.awesomengwin.kingfisher.lyrics.TranslationLyricsStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
                .anyMatch(line -> line.translatedWords() != null
                        && !line.translatedWords().isBlank());

        if (!isPartialTranslated) {
            this.translateStatus = TranslationLyricsStatus.NONE;
            return;
        }

        boolean isCompletedTranslated = this.lines.stream()
                .allMatch(line -> line.translatedWords() != null
                        && !line.translatedWords().isBlank());

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
