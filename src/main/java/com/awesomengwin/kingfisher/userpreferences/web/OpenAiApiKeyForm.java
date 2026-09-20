package com.awesomengwin.kingfisher.userpreferences.web;

import jakarta.validation.constraints.NotBlank;

public record OpenAiApiKeyForm(
        @NotBlank(message = "OpenAI API Key must not be blank") String openAiApiKey
) {
}
