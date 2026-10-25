create table buyers (
    id uuid primary key,
    identity_id uuid not null unique
);
