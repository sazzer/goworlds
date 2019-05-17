CREATE TABLE users
(
    user_id  UUID PRIMARY KEY,
    version  UUID      NOT NULL,
    created  TIMESTAMP NOT NULL,
    updated  TIMESTAMP NOT NULL,
    name     TEXT      NOT NULL,
    email    TEXT      NOT NULL UNIQUE,
    password TEXT      NOT NULL
);
