CREATE TABLE user_preferences
(
    user_id        varchar(10) NOT NULL,
    openai_api_key text,
    CONSTRAINT user_preferences_pk PRIMARY KEY (user_id)
);
