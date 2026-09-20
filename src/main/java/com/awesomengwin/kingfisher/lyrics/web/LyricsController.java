package com.awesomengwin.kingfisher.lyrics.web;

import com.awesomengwin.kingfisher.lyrics.LyricsTranslationDto;
import com.awesomengwin.kingfisher.lyrics.LyricsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
                            @RequestParam String trackId, Model model, HttpServletResponse response) {
        LyricsTranslationDto lyrics = lyricsService.getLyrics(trackId, currentUser.getName());
        model.addAttribute("lyrics", lyrics);

        response.addHeader("HX-Trigger", "lyrics:init");

        return "lyrics/lyrics";
    }

    @PutMapping("/translate")
    public String translateLyrics(@AuthenticationPrincipal OAuth2User currentUser,
                                  @RequestParam String trackId, Model model, HttpServletResponse response) {
        LyricsTranslationDto lyrics = lyricsService.translate(trackId, currentUser.getName());
        model.addAttribute("lyrics", lyrics);

        response.addHeader("HX-Trigger", "lyrics:init");

        return "lyrics/lyrics";
    }
}
