package com.awesomengwin.kingfisher.lyrics;

import com.awesomengwin.kingfisher.common.AggregateRoot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Translation extends AggregateRoot {

    private final String trackId;
    private final String userId;
    private List<TranslationLine> lines = new ArrayList<>();
    private TranslationStatus status;

    public Translation(String trackId, String userId) {
        if (trackId == null) {
            throw new IllegalArgumentException("trackId must not be null");
        }

        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }

        this.trackId = trackId;
        this.userId = userId;
        this.status = TranslationStatus.PROCESSING;
        registerEvent(new TranslationCreated(trackId, userId));
    }

    private Translation(String trackId, String userId, List<TranslationLine> lines, TranslationStatus status) {
        this.trackId = trackId;
        this.userId = userId;
        this.lines = new ArrayList<>(lines);
        this.status = status;
    }

    public static Translation reconstitute(String trackId, String userId, List<TranslationLine> lines, TranslationStatus status) {
        return new Translation(trackId, userId, lines, status);
    }

    public void markCompleted(List<TranslationLine> lines) {
        if (this.status != TranslationStatus.PROCESSING) {
            throw new IllegalStateException("Translation must be in state processing before completed");
        }

        this.lines = new ArrayList<>(lines);
        this.status = TranslationStatus.COMPLETED;
    }

    public boolean isCompleted() {
        return status == TranslationStatus.COMPLETED;
    }

    public String getTrackId() {
        return trackId;
    }

    public String getUserId() {
        return userId;
    }

    public List<TranslationLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public TranslationStatus getStatus() {
        return status;
    }
}
