package com.awesomengwin.kingfisher.config;

import com.awesomengwin.kingfisher.common.ApiClientException;
import com.awesomengwin.kingfisher.common.ApiClientNotFoundException;
import com.awesomengwin.kingfisher.common.ApiServerException;
import com.awesomengwin.kingfisher.lyrics.client.LyricsApiErrorResponse;
import com.awesomengwin.kingfisher.lyrics.client.LyricsClient;
import com.awesomengwin.kingfisher.spotify.client.SpotifyApiErrorResponse;
import com.awesomengwin.kingfisher.spotify.client.SpotifyClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.support.OAuth2RestClientHttpServiceGroupConfigurer;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.ImportHttpServices;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;

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
    public RestClientHttpServiceGroupConfigurer apiClientErrorHandling(JsonMapper jsonMapper) {
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
            return getResponseBodyAs(SpotifyApiErrorResponse.class, response, jsonMapper).error().message();
        } else if ("lyrics".equals(groupName)) {
            return getResponseBodyAs(LyricsApiErrorResponse.class, response, jsonMapper).message();
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
