package com.awesomengwin.kingfisher.config;

import com.awesomengwin.kingfisher.common.ApiClientException;
import com.awesomengwin.kingfisher.common.ApiClientNotFoundException;
import com.awesomengwin.kingfisher.common.ApiServerException;
import com.awesomengwin.kingfisher.lyrics.client.LyricsClient;
import com.awesomengwin.kingfisher.spotify.client.SpotifyClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.support.OAuth2RestClientHttpServiceGroupConfigurer;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
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

    @Bean
    public RestClientHttpServiceGroupConfigurer apiClientErrorHandling() {
        return groups -> groups.forEachClient((group, clientBuilder) -> {
            String groupName = group.name();

            clientBuilder.defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                HttpStatusCode statusCode = response.getStatusCode();

                String message = String.format("Api client error, group: %s, status: %s, uri: %s",
                        groupName, statusCode, request.getURI());

                if (statusCode == HttpStatus.NOT_FOUND) {
                    throw new ApiClientNotFoundException(groupName, message);
                }
                throw new ApiClientException(groupName, statusCode, message);
            });

            clientBuilder.defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) -> {
                HttpStatusCode statusCode = response.getStatusCode();
                String message = String.format("Api server error, group: %s, status: %s, uri: %s",
                        groupName, statusCode, request.getURI());
                throw new ApiServerException(groupName, statusCode, message);
            });
        });
    }
}
