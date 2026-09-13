package com.awesomengwin.kingfisher.spotify.impl;

public record SpotifyCacheKey(
        String userId,
        String cacheId,
        String resourceId,
        int limit,
        int offset
) {
    public SpotifyCacheKey(String userId, String cacheId, int limit, int offset) {
        this(userId, cacheId, null, limit, offset);
    }
}
