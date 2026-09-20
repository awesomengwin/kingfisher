package com.awesomengwin.kingfisher.userpreferences.web;

import com.awesomengwin.kingfisher.userpreferences.UserPreferencesService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/me/preferences")
public class UserPreferencesController {

    private final UserPreferencesService userPreferencesService;

    public UserPreferencesController(UserPreferencesService userPreferencesService) {
        this.userPreferencesService = userPreferencesService;
    }

    @GetMapping
    public String preferences(@AuthenticationPrincipal OAuth2User currentUser, Model model) {
        model.addAttribute("userPrefs",
                userPreferencesService.getUserPrefs(currentUser.getName()));

        return "userpreferences/preferences";
    }

    @PostMapping("/openai")
    public String updateOpenAiApiKey(@AuthenticationPrincipal OAuth2User currentUser,
                                     @RequestParam String openAiApiKey, Model model) {
        userPreferencesService.updateOpenAiApiKey(currentUser.getName(), openAiApiKey);
        model.addAttribute("userPrefs",
                userPreferencesService.getUserPrefs(currentUser.getName()));

        return "userpreferences/preferences";
    }

    @PostMapping("/openai/delete")
    public String deleteOpenAiApiKey(@AuthenticationPrincipal OAuth2User currentUser, Model model) {
        userPreferencesService.deleteOpenAiApiKey(currentUser.getName());
        model.addAttribute("userPrefs",
                userPreferencesService.getUserPrefs(currentUser.getName()));

        return "userpreferences/preferences";
    }
}
