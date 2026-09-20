CREATE TABLE translation
(
    id               bigserial PRIMARY KEY,
    track_id         varchar(22) NOT NULL,
    user_id          varchar(10) NOT NULL,
    lines            jsonb,
    translate_status varchar(10),
    UNIQUE (track_id, user_id)
);
