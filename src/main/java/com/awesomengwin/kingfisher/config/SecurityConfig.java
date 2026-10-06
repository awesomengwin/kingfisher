package com.awesomengwin.kingfisher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.*;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestClient;

@Configuration
public class SecurityConfig {

    private final RestClient jdkRestClient;

    public SecurityConfig() {
        jdkRestClient = RestClient.builder()
                .requestFactory(new JdkClientHttpRequestFactory())
                .configureMessageConverters(c -> c
                        .addCustomConverter(new FormHttpMessageConverter())
                        .addCustomConverter(new OAuth2AccessTokenResponseHttpMessageConverter()))
                .defaultStatusHandler(new OAuth2ErrorResponseErrorHandler())
                .build();
    }

    @Bean
    OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> authorizationCodeAccessTokenResponseClient() {
        RestClientAuthorizationCodeTokenResponseClient authorizationCodeResponseClient =
                new RestClientAuthorizationCodeTokenResponseClient();
        authorizationCodeResponseClient.setRestClient(jdkRestClient);

        return authorizationCodeResponseClient;
    }

    @Bean
    OAuth2AccessTokenResponseClient<OAuth2RefreshTokenGrantRequest> refreshTokenAccessTokenResponseClient() {
        RestClientRefreshTokenTokenResponseClient refreshTokenResponseClient =
                new RestClientRefreshTokenTokenResponseClient();
        refreshTokenResponseClient.setRestClient(jdkRestClient);

        return refreshTokenResponseClient;
    }

    @Bean
    OAuth2AccessTokenResponseClient<OAuth2ClientCredentialsGrantRequest> clientCredentialsAccessTokenResponseClient() {
        RestClientClientCredentialsTokenResponseClient clientCredentialsResponseClient =
                new RestClientClientCredentialsTokenResponseClient();
        clientCredentialsResponseClient.setRestClient(jdkRestClient);

        return clientCredentialsResponseClient;
    }

    @Bean
    OAuth2AuthorizedClientService jdbcOAuth2AuthorizedClientService(
            JdbcOperations jdbcOperations,
            ClientRegistrationRepository clientRegistrationRepository) {
        return new JdbcOAuth2AuthorizedClientService(jdbcOperations, clientRegistrationRepository);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(Customizer.withDefaults());

        return http.build();
    }
}
