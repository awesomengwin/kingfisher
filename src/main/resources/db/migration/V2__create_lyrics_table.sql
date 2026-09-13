CREATE TABLE lyrics
(
    id       bigserial PRIMARY KEY,
    track_id varchar(22) NOT NULL,
    lines    jsonb
);
