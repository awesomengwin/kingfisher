CREATE TABLE translation_lyrics
(
    track_id varchar(22) NOT NULL,
    user_id  varchar(10) NOT NULL,
    lines    jsonb,
    CONSTRAINT translation_lyrics_pk PRIMARY KEY (track_id, user_id)
);
