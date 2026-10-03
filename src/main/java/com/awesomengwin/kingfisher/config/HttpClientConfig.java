package com.awesomengwin.kingfisher.config;

import com.awesomengwin.kingfisher.common.ApiClientException;
import com.awesomengwin.kingfisher.common.ApiClientNotFoundException;
import com.awesomengwin.kingfisher.common.ApiServerException;
import com.awesomengwin.kingfisher.lyrics.spotifylyrics.SpotifyLyricsApiError;
import com.awesomengwin.kingfisher.lyrics.spotifylyrics.SpotifyLyricsClient;
import com.awesomengwin.kingfisher.spotify.SpotifyApiError;
import com.awesomengwin.kingfisher.spotify.SpotifyClient;
import com.awesomengwin.kingfisher.wikipedia.WikipediaClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.support.OAuth2RestClientHttpServiceGroupConfigurer;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.ImportHttpServices;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@ImportHttpServices(group = "spotify", types = SpotifyClient.class)
@ImportHttpServices(group = "lyrics", types = SpotifyLyricsClient.class)
@ImportHttpServices(group = "wikipedia", types = WikipediaClient.class)
public class HttpClientConfig {

    @Bean
    OAuth2RestClientHttpServiceGroupConfigurer oauth2RestClientConfigurer(
            OAuth2AuthorizedClientManager manager) {
        return OAuth2RestClientHttpServiceGroupConfigurer.from(manager);
    }

    @Bean
    RestClientHttpServiceGroupConfigurer requestFactoryConfigurer() {
        return groups -> groups.forEachClient((group, clientBuilder) -> clientBuilder
                .requestFactory(new JdkClientHttpRequestFactory()));
    }

    @Bean
    RestClientHttpServiceGroupConfigurer wikipediaUserAgentInterceptor(@Value("${kingfisher.user-agent}") String userAgent) {
        return groups -> groups.filterByName("wikipedia")
                .forEachClient((group, clientBuilder) -> clientBuilder
                        .defaultHeader(HttpHeaders.USER_AGENT, userAgent));
    }

    @Bean
    RestClientHttpServiceGroupConfigurer apiClientErrorHandling(JsonMapper jsonMapper) {
        return groups -> groups.forEachClient((group, clientBuilder) -> {
            String groupName = group.name();

            clientBuilder.defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                HttpStatusCode statusCode = response.getStatusCode();
                String message = getErrorMessage(groupName, response, jsonMapper);

                if (statusCode == HttpStatus.NOT_FOUND) {
                    throw new ApiClientNotFoundException(groupName, response.getStatusText(), message);
                }
                throw new ApiClientException(groupName, statusCode, response.getStatusText(), message);
            });

            clientBuilder.defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) -> {
                HttpStatusCode statusCode = response.getStatusCode();
                String message = getErrorMessage(groupName, response, jsonMapper);

                throw new ApiServerException(groupName, statusCode, response.getStatusText(), message);
            });
        });
    }

    private static String getErrorMessage(String groupName, ClientHttpResponse response, JsonMapper jsonMapper) {
        if ("spotify".equals(groupName)) {
            return getResponseBodyAs(SpotifyApiError.class, response, jsonMapper).error().message();
        } else if ("lyrics".equals(groupName)) {
            return getResponseBodyAs(SpotifyLyricsApiError.class, response, jsonMapper).message();
        } else throw new RuntimeException("Unknown group name");
    }

    private static <T> T getResponseBodyAs(Class<T> targetType, ClientHttpResponse response, JsonMapper jsonMapper) {
        try (InputStream body = response.getBody()) {
            return jsonMapper.readValue(body.readAllBytes(), targetType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
