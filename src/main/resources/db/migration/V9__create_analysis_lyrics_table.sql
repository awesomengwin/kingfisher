CREATE TABLE analysis_lyrics
(
    track_id       varchar(22) NOT NULL,
    user_id        varchar(10) NOT NULL,
    annotations    jsonb,
    status         varchar(10),
    failure_reason text,
    CONSTRAINT analysis_lyrics_pk PRIMARY KEY (track_id, user_id)
);
