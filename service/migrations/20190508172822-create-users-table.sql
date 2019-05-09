
-- +migrate Up
CREATE TABLE users(
  user_id UUID PRIMARY KEY,
  version UUID NOT NULL,
  created TIMESTAMP NOT NULL,
  updated TIMESTAMP NOT NULL,
  name TEXT NOT NULL UNIQUE,
  email TEXT NOT NULL UNIQUE,
  password TEXT NULL
);

-- +migrate Down
DROP TABLE users;
