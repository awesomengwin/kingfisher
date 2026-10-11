package com.awesomengwin.kingfisher.lyrics;

import java.util.Optional;

public interface AnalysisRepository {

    Optional<Analysis> findByTrackIdAndUserId(String trackId, String userId);

    void save(Analysis analysis);

}
