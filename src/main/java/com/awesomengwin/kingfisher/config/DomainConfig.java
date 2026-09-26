package com.awesomengwin.kingfisher.config;

import com.awesomengwin.kingfisher.caffeine.CaffeineLibraryService;
import com.awesomengwin.kingfisher.library.LibraryService;
import com.awesomengwin.kingfisher.spotify.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    LibraryService libraryService(SpotifyClient spotifyClient,
                                  SpotifyUserSavedTrackMapper spotifyUserSavedTrackMapper,
                                  SpotifyUserSavedAlbumMapper spotifyUserSavedAlbumMapper,
                                  SpotifyUserPlaylistMapper spotifyUserPlaylistMapper,
                                  SpotifyPlaylistTrackMapper spotifyPlaylistTrackMapper,
                                  SpotifyAlbumTrackMapper spotifyAlbumTrackMapper,
                                  SpotifyTrackMapper spotifyTrackMapper) {
        SpotifyLibraryService spotifyLibraryService = new SpotifyLibraryService(
                spotifyClient,
                spotifyUserSavedTrackMapper,
                spotifyUserSavedAlbumMapper,
                spotifyUserPlaylistMapper,
                spotifyPlaylistTrackMapper,
                spotifyAlbumTrackMapper,
                spotifyTrackMapper
        );

        return new CaffeineLibraryService(spotifyLibraryService);
    }
}
