package com.awesomengwin.kingfisher.spotify.web;

import com.awesomengwin.kingfisher.spotify.SpotifyService;
import com.awesomengwin.kingfisher.spotify.dto.Page;
import com.awesomengwin.kingfisher.spotify.dto.response.SavedTrack;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/library")
public class SpotifyLibraryController {

    private final SpotifyService spotifyService;

    public SpotifyLibraryController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/tracks")
    public String userSavedTracks(@AuthenticationPrincipal OAuth2User currentUser,
                                  @RequestParam(defaultValue = "20") int limit,
                                  @RequestParam(defaultValue = "0") int offset,
                                  Model model) {
        Page<SavedTrack> userSavedTracks =
                spotifyService.getUserSavedTracks(currentUser.getName(), limit, offset);
        model.addAttribute("userSavedTracks", userSavedTracks);

        return "library/user-saved-tracks";
    }
}
