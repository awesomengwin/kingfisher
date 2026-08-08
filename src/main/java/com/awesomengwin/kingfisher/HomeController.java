package com.awesomengwin.kingfisher;

import com.awesomengwin.kingfisher.spotify.SpotifyUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

    @ModelAttribute("currentUser")
    public SpotifyUser currentUser(@AuthenticationPrincipal OAuth2User oauth2User) {
        return new SpotifyUser(oauth2User);
    }

    @GetMapping
    public String index() {
        return "index";
    }
}
