package com.awesomengwin.kingfisher;

import com.awesomengwin.kingfisher.spotify.SpotifyService;
import com.awesomengwin.kingfisher.spotify.SpotifyUser;
import com.awesomengwin.kingfisher.spotify.valueobject.Page;
import com.awesomengwin.kingfisher.spotify.valueobject.SavedTrack;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final SpotifyService spotifyService;

    public HomeController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @ModelAttribute
    public void init(@AuthenticationPrincipal OAuth2User oauth2User,
                     @RequestParam(defaultValue = "20") int limit,
                     @RequestParam(defaultValue = "0") int offset,
                     Model model) {
        SpotifyUser spotifyUser = new SpotifyUser(oauth2User);

        Page<SavedTrack> userSavedTracks =
                spotifyService.getUserSavedTracks(spotifyUser.getName(), limit, offset);

        model.addAttribute("currentUser", spotifyUser);
        model.addAttribute("userSavedTracks", userSavedTracks);
    }

    @GetMapping
    public String index() {
        return "index";
    }
}
