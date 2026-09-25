package com.awesomengwin.kingfisher.spotifylegacy.web;

import com.awesomengwin.kingfisher.spotifylegacy.SpotifyService;
import com.awesomengwin.kingfisher.spotifylegacy.client.PlaylistResponse;
import com.awesomengwin.kingfisher.spotifylegacy.client.PlaylistTrackResponse;
import com.awesomengwin.kingfisher.spotifylegacy.client.SpotifyPage;
import com.awesomengwin.kingfisher.spotifylegacy.client.SavedTrackResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/playlists")
    public String userPlaylists(@AuthenticationPrincipal OAuth2User currentUser,
                                @ModelAttribute PaginationRequest p,
                                Model model, HttpServletResponse response) {
        SpotifyPage<PlaylistResponse> userPlaylists =
                spotifyService.getUserPlaylists(currentUser.getName(), p.limit(), p.offset());
        model.addAttribute("userPlaylists", userPlaylists);

        response.addHeader("HX-Trigger", "user-playlists:init");

        return "library/user-playlists";
    }

    @GetMapping("/playlists/{playlistId}/tracks")
    public String playlistTracks(@AuthenticationPrincipal OAuth2User currentUser,
                                 @PathVariable String playlistId,
                                 @ModelAttribute PaginationRequest p,
                                 Model model, HttpServletResponse response) {
        SpotifyPage<PlaylistTrackResponse> playlistTracks =
                spotifyService.getPlaylistTracks(currentUser.getName(), playlistId, p.limit(), p.offset());
        model.addAttribute("playlistTracks", playlistTracks);
        model.addAttribute("playlistUri", "spotify:playlist:%s".formatted(playlistId));

        response.addHeader("HX-Trigger", "playlist-tracks:init");

        return "library/playlist-tracks";
    }
}
