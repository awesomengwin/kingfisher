package com.awesomengwin.kingfisher.userpreferences;

import org.springframework.stereotype.Service;

@Service
public class UserPreferencesService {

    private final UserPreferencesRepository userPreferencesRepository;

    public UserPreferencesService(UserPreferencesRepository userPreferencesRepository) {
        this.userPreferencesRepository = userPreferencesRepository;
    }

    public void updateOpenaiApiKey(String userId, String openaiApiKey) {
        UserPreferences userPrefs = userPreferencesRepository.findById(userId)
                .orElseGet(() -> new UserPreferences(userId));

        userPrefs.setOpenaiApiKey(openaiApiKey);

        userPreferencesRepository.save(userPrefs);
    }

    public void deleteOpenaiApiKey(String userId) {
        UserPreferences userPrefs = userPreferencesRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "User Prefs with ID %s could not be found".formatted(userId)));

        userPrefs.setOpenaiApiKey(null);

        userPreferencesRepository.save(userPrefs);
    }

    public UserPreferencesDto getUserPrefs(String userId) {
        UserPreferences userPrefs = userPreferencesRepository.findById(userId).orElse(null);
        return userPrefs == null ? null : new UserPreferencesDto(
                userPrefs.getUserId(), mask(userPrefs.getOpenaiApiKey()));
    }

    private String mask(String key) {
        if (key == null) return null;

        return key.length() < 8 ? "sk-...." : "sk-..." + key.substring(key.length() - 4);
    }
}
