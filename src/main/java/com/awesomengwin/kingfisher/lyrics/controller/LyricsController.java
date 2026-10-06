package com.awesomengwin.kingfisher.lyrics.controller;

import com.awesomengwin.kingfisher.lyrics.Lyrics;
import com.awesomengwin.kingfisher.lyrics.LyricsService;
import com.awesomengwin.kingfisher.lyrics.Translation;
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

    @GetMapping("/translation")
    public String pollTranslation(@AuthenticationPrincipal OAuth2User currentUser,
                                  @RequestParam String trackId, Model model, HttpServletResponse resp) {
        Translation translation = lyricsService.getTranslation(trackId, currentUser.getName());
        setupForTranslation(trackId, translation, model);

        if (translation.isCompleted()) {
            resp.addHeader("HX-Trigger", "translation:completed");
        }

        return "lyrics/lyrics-translation";
    }

    @PostMapping("/translate")
    public String translateLyrics(@AuthenticationPrincipal OAuth2User currentUser,
                                  @RequestParam String trackId, Model model) {
        Translation translation = lyricsService.translateLyrics(trackId, currentUser.getName());
        setupForTranslation(trackId, translation, model);

        return "lyrics/lyrics-translation";
    }

    private void setupForTranslation(String trackId, Translation translation, Model model) {
        model.addAttribute("trackId", trackId);
        model.addAttribute("isProcessing", translation.isProcessing());
        model.addAttribute("isCompleted", translation.isCompleted());
        model.addAttribute("isFailed", translation.isFailed());
        model.addAttribute("failureReason", translation.getFailureReason());
    }
}
