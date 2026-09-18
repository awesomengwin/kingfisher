package com.awesomengwin.kingfisher.lyrics;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

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

    @Enumerated(EnumType.STRING)
    private TranslateStatus translateStatus;

    public Lyrics() {
    }

    public Lyrics(String trackId, List<LyricsLine> lines) {
        this.trackId = trackId;
        this.lines = lines;

        boolean isPartialTranslated = lines.stream().anyMatch(line -> StringUtils.hasText(line.translatedWords()));

        if (isPartialTranslated) {
            this.translateStatus = TranslateStatus.PARTIAL;

            boolean isCompletedTranslated = lines.stream().allMatch(line -> StringUtils.hasText(line.translatedWords()));

            if (isCompletedTranslated) {
                this.translateStatus = TranslateStatus.COMPLETED;
            }
        } else {
            this.translateStatus = TranslateStatus.NONE;
        }
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

    public TranslateStatus getTranslateStatus() {
        return translateStatus;
    }
}
