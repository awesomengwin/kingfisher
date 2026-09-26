package com.awesomengwin.kingfisher.spotify;

public record SpotifyApiError(Error error) {

    public record Error(int status, String message) {
    }
}
