create table sellers (
    id uuid primary key,
    identity_id uuid not null unique
);
