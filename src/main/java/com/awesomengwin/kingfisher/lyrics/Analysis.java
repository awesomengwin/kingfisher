package com.awesomengwin.kingfisher.lyrics;

import com.awesomengwin.kingfisher.common.AggregateRoot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Analysis extends AggregateRoot {

    private final String trackId;
    private final String userId;
    private List<AnalysisAnnotation> annotations = new ArrayList<>();
    private AnalysisStatus status;
    private String failureReason;

    public Analysis(String trackId, String userId) {
        if (trackId == null) {
            throw new IllegalArgumentException("trackId must not be null");
        }

        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }

        this.trackId = trackId;
        this.userId = userId;
        this.status = AnalysisStatus.PROCESSING;
        registerEvent(new AnalysisCreated(trackId, userId));
    }

    private Analysis(String trackId, String userId, List<AnalysisAnnotation> annotations, AnalysisStatus status, String failureReason) {
        this.trackId = trackId;
        this.userId = userId;
        this.annotations = new ArrayList<>(annotations);
        this.status = status;
        this.failureReason = failureReason;
    }

    public static Analysis reconstitute(String trackId, String userId, List<AnalysisAnnotation> annotations, AnalysisStatus status, String failureReason) {
        return new Analysis(trackId, userId, annotations, status, failureReason);
    }

    public void markCompleted(List<AnalysisAnnotation> annotations) {
        if (this.status != AnalysisStatus.PROCESSING) {
            throw new IllegalStateException("Analysis must be in state processing before completed");
        }

        this.annotations = new ArrayList<>(annotations);
        this.status = AnalysisStatus.COMPLETED;
    }

    public void markFailed(String failureReason) {
        this.status = AnalysisStatus.FAILED;
        this.failureReason = failureReason;
    }

    public boolean isCompleted() {
        return status == AnalysisStatus.COMPLETED;
    }

    public boolean isFailed() {
        return status == AnalysisStatus.FAILED;
    }

    public String getTrackId() {
        return trackId;
    }

    public String getUserId() {
        return userId;
    }

    public List<AnalysisAnnotation> getAnnotations() {
        return Collections.unmodifiableList(annotations);
    }

    public AnalysisStatus getStatus() {
        return status;
    }

    public String getFailureReason() {
        return failureReason;
    }
}
