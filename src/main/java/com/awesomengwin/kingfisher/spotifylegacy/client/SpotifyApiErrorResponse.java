package com.awesomengwin.kingfisher.spotifylegacy.client;

public record SpotifyApiErrorResponse(SpotifyApiError error) {

    public record SpotifyApiError(int status, String message) {
    }
}
