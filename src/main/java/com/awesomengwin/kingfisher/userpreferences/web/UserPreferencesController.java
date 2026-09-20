package com.awesomengwin.kingfisher.userpreferences.web;

import com.awesomengwin.kingfisher.userpreferences.UserPreferencesService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        model.addAttribute("openAiApiKeyForm", new OpenAiApiKeyForm(null));

        return "userpreferences/preferences";
    }

    @PostMapping("/openai")
    public String updateOpenAiApiKey(@AuthenticationPrincipal OAuth2User currentUser,
                                     @Valid @ModelAttribute("openAiApiKeyForm") OpenAiApiKeyForm form,
                                     BindingResult result) {
        if (result.hasErrors()) {
            return "userpreferences/preferences";
        }

        userPreferencesService.updateOpenAiApiKey(currentUser.getName(), form.openAiApiKey());

        return "redirect:/me/preferences";
    }

    @PostMapping("/openai/delete")
    public String deleteOpenAiApiKey(@AuthenticationPrincipal OAuth2User currentUser) {
        userPreferencesService.deleteOpenAiApiKey(currentUser.getName());

        return "redirect:/me/preferences";
    }
}
