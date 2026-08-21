package com.awesomengwin.kingfisher.config;

import com.awesomengwin.kingfisher.lyrics.client.LyricsClient;
import com.awesomengwin.kingfisher.spotify.client.SpotifyClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.support.OAuth2RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(group = "spotify", types = SpotifyClient.class)
@ImportHttpServices(group = "lyrics", types = LyricsClient.class)
public class HttpClientConfig {

    @Bean
    public OAuth2RestClientHttpServiceGroupConfigurer oauth2RestClientConfigurer(
            OAuth2AuthorizedClientManager manager) {
        return OAuth2RestClientHttpServiceGroupConfigurer.from(manager);
    }
}
