package com.awesomengwin.kingfisher.userpreferences;

import org.springframework.stereotype.Service;

@Service
public class UserPreferencesService {

    private final UserPreferencesRepository userPreferencesRepository;

    public UserPreferencesService(UserPreferencesRepository userPreferencesRepository) {
        this.userPreferencesRepository = userPreferencesRepository;
    }

    public void updateOpenAiApiKey(String userId, String openAiApiKey) {
        UserPreferences userPrefs = userPreferencesRepository.findById(userId)
                .orElseGet(() -> new UserPreferences(userId));

        userPrefs.setOpenAiApiKey(openAiApiKey);

        userPreferencesRepository.save(userPrefs);
    }

    public void deleteOpenAiApiKey(String userId) {
        UserPreferences userPrefs = userPreferencesRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "User Prefs with ID %s could not be found".formatted(userId)));

        userPrefs.setOpenAiApiKey(null);

        userPreferencesRepository.save(userPrefs);
    }

    public UserPreferencesDto getUserPrefs(String userId) {
        UserPreferences userPrefs = userPreferencesRepository.findById(userId).orElse(null);
        return userPrefs == null ? null : new UserPreferencesDto(
                userPrefs.getUserId(), mask(userPrefs.getOpenAiApiKey()));
    }

    private String mask(String key) {
        if (key == null) return null;

        return key.length() < 8 ? "sk-...." : "sk-..." + key.substring(key.length() - 4);
    }
}
