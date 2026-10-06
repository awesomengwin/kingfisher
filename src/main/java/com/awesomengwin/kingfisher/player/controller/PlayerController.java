package com.awesomengwin.kingfisher.player.controller;

import com.awesomengwin.kingfisher.player.PlayerService;
import com.awesomengwin.kingfisher.player.RepeatMode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(RepeatMode.class, new RepeatModeEditor());
    }

    @PutMapping("/play")
    public void play(@AuthenticationPrincipal OAuth2User currentUser,
                     @RequestParam String deviceId,
                     @RequestParam(required = false) String contextUri, @RequestParam String uri) {
        playerService.play(currentUser.getName(), deviceId, contextUri, uri);
    }

    @PutMapping("/shuffle")
    public void shuffle(@RequestParam String deviceId, @RequestParam boolean enabled) {
        playerService.shuffle(deviceId, enabled);
    }

    @PutMapping("/repeat")
    public void repeat(@RequestParam String deviceId, @RequestParam RepeatMode mode) {
        playerService.repeat(deviceId, mode);
    }
}
