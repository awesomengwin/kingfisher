package com.awesomengwin.kingfisher.common;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {

    @PostMapping("/token")
    public String getSpotifyOAuth2Token(
            @RegisteredOAuth2AuthorizedClient("spotify-login") OAuth2AuthorizedClient authorizedClient) {
        return authorizedClient.getAccessToken().getTokenValue();
    }
}
