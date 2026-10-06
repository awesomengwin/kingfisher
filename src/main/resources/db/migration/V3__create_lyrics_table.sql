CREATE TABLE lyrics
(
    track_id varchar(22) NOT NULL,
    lines    jsonb,
    CONSTRAINT lyrics_pk PRIMARY KEY (track_id)
);
