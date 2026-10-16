package com.awesomengwin.kingfisher.player;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Device(
        String id,
        String name,
        @JsonProperty("is_active")
        boolean isActive
) {
}
