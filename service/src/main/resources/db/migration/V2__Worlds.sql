CREATE TABLE worlds
(
    world_id       UUID PRIMARY KEY,
    version        UUID      NOT NULL,
    created        TIMESTAMP NOT NULL,
    updated        TIMESTAMP NOT NULL,
    name           TEXT      NOT NULL,
    owner_id       UUID      NOT NULL REFERENCES users (user_id),
    description    TEXT      NOT NULL
);
