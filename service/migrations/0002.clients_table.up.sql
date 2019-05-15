CREATE TABLE oauth2_clients (
    client_id UUID PRIMARY KEY,
    version UUID NOT NULL,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL,
    name TEXT NOT NULL,
    client_secret TEXT NOT NULL,
    owner_id UUID NOT NULL REFERENCES users(user_id),
    redirect_uris TEXT[] NOT NULL,
    response_types TEXT[] NOT NULL,
    grant_types TEXT[] NOT NULL
);
