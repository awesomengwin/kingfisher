package com.awesomengwin.kingfisher.config;

import com.awesomengwin.kingfisher.caffeine.CaffeineLibraryService;
import com.awesomengwin.kingfisher.library.LibraryService;
import com.awesomengwin.kingfisher.spotify.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    LibraryService libraryService(SpotifyClient spotifyClient) {
        return new CaffeineLibraryService(new SpotifyLibraryService(spotifyClient));
    }
}
