package com.awesomengwin.kingfisher.library.controller;

import com.awesomengwin.kingfisher.library.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/library")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/tracks")
    public String userSavedTracks(@AuthenticationPrincipal OAuth2User currentUser,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "20") int size,
                                  Model model, HttpServletResponse resp) {
        Page<UserSavedTrack> userSavedTracks =
                libraryService.getUserSavedTracks(currentUser.getName(), new Pageable(page, size));
        model.addAttribute("userSavedTracks", userSavedTracks);

        resp.addHeader("HX-Trigger", "user-saved-tracks:init");

        return "library/user-saved-tracks";
    }

    @GetMapping("/playlists")
    public String userPlaylists(@AuthenticationPrincipal OAuth2User currentUser,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "20") int size,
                                Model model, HttpServletResponse resp) {
        Page<UserPlaylist> userPlaylists =
                libraryService.getUserPlaylists(currentUser.getName(), new Pageable(page, size));
        model.addAttribute("userPlaylists", userPlaylists);

        resp.addHeader("HX-Trigger", "user-playlists:init");

        return "library/user-playlists";
    }

    @GetMapping("/playlists/{playlistId}/tracks")
    public String playlistTracks(@AuthenticationPrincipal OAuth2User currentUser,
                                 @PathVariable String playlistId,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "20") int size,
                                 Model model, HttpServletResponse resp) {
        Page<Track> playlistTracks =
                libraryService.getPlaylistTracks(playlistId, currentUser.getName(), new Pageable(page, size));
        model.addAttribute("playlistTracks", playlistTracks);
        model.addAttribute("playlistUri", "spotify:playlist:%s".formatted(playlistId));

        resp.addHeader("HX-Trigger", "playlist-tracks:init");

        return "library/playlist-tracks";
    }
}
