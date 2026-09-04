package com.awesomengwin.kingfisher.spotify.web;

import com.awesomengwin.kingfisher.spotify.SpotifyService;
import com.awesomengwin.kingfisher.spotify.client.SpotifyPage;
import com.awesomengwin.kingfisher.spotify.client.SavedTrackResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/library")
public class SpotifyLibraryController {

    private final SpotifyService spotifyService;

    public SpotifyLibraryController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/tracks")
    public String userSavedTracks(@AuthenticationPrincipal OAuth2User currentUser,
                                  @ModelAttribute PaginationRequest p,
                                  Model model, HttpServletResponse response) {
        SpotifyPage<SavedTrackResponse> userSavedTracks =
                spotifyService.getUserSavedTracks(currentUser.getName(), p.limit(), p.offset());
        model.addAttribute("userSavedTracks", userSavedTracks);

        response.addHeader("HX-Trigger", "user-saved-tracks:init");

        return "library/user-saved-tracks";
    }
}
