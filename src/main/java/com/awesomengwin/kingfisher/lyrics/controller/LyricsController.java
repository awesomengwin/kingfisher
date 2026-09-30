package com.awesomengwin.kingfisher.lyrics.controller;

import com.awesomengwin.kingfisher.lyrics.Lyrics;
import com.awesomengwin.kingfisher.lyrics.LyricsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/lyrics")
public class LyricsController {

    private final LyricsService lyricsService;

    public LyricsController(LyricsService lyricsService) {
        this.lyricsService = lyricsService;
    }

    @GetMapping
    public String getLyrics(@AuthenticationPrincipal OAuth2User currentUser,
                            @RequestParam String trackId, Model model, HttpServletResponse resp) {
        Lyrics lyrics = lyricsService.getLyrics(trackId, currentUser.getName());
        model.addAttribute("lyrics", lyrics);

        resp.addHeader("HX-Trigger", "lyrics:init");

        return "lyrics/lyrics";
    }

    @PostMapping("/translate")
    public String translateLyrics(@AuthenticationPrincipal OAuth2User currentUser,
                                  @RequestParam String trackId, Model model, HttpServletResponse resp) {
        Lyrics lyrics = lyricsService.translateLyrics(trackId, currentUser.getName());
        model.addAttribute("lyrics", lyrics);

        resp.addHeader("HX-Trigger", "lyrics:init");

        return "lyrics/lyrics";
    }
}
