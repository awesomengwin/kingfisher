package com.awesomengwin.kingfisher.userpreferences;

import jakarta.persistence.*;

@Entity
@Table(name = "user_preferences")
public class UserPreferences {

    @Id
    private String userId;

    @Convert(converter = ApiKeyConverter.class)
    @Column(name = "openai_api_key")
    private String openAiApiKey;

    public UserPreferences() {
    }

    public UserPreferences(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getOpenAiApiKey() {
        return openAiApiKey;
    }

    public void setOpenAiApiKey(String openAiApiKey) {
        this.openAiApiKey = openAiApiKey;
    }
}
