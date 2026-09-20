package com.awesomengwin.kingfisher.userpreferences;

import org.springframework.stereotype.Service;

@Service
public class OpenAiApiKeyProvider {

    private final UserPreferencesRepository userPreferencesRepository;

    public OpenAiApiKeyProvider(UserPreferencesRepository userPreferencesRepository) {
        this.userPreferencesRepository = userPreferencesRepository;
    }

    public String getApiKey(String userId) {
        UserPreferences userPrefs = userPreferencesRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "User prefs with ID %s could not be found".formatted(userId)));

        return userPrefs.getOpenAiApiKey();
    }
}
