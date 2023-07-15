create table oauth2_credentials
(
    auth_server  varchar(255) not null,
    external_id  varchar(255) not null,
    created_at   timestamp(6) not null,
    presentation varchar(255) not null,
    user_id      bigint references users (id) not null,
    primary key (auth_server, external_id, user_id)
);