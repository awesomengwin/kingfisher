package com.awesomengwin.kingfisher.lyrics.web;

import com.awesomengwin.kingfisher.lyrics.LyricsDto;
import com.awesomengwin.kingfisher.lyrics.LyricsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String getLyrics(@RequestParam String trackId, Model model, HttpServletResponse response) {
        LyricsDto lyrics = lyricsService.getLyrics(trackId);
        model.addAttribute("lyrics", lyrics);

        response.addHeader("HX-Trigger", "lyrics:init");

        return "lyrics/lyrics";
    }
}
