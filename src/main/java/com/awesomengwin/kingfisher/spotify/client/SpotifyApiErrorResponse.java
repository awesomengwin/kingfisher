package com.awesomengwin.kingfisher.spotify.client;

public record SpotifyApiErrorResponse(SpotifyApiError error) {

    public record SpotifyApiError(int status, String message) {
    }
}
