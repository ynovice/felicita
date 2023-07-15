create table articles
(
    id         bigserial primary key,
    author     varchar(255) not null,
    content    varchar(50000) not null,
    created_at timestamp(6) not null,
    name       varchar(200) not null,
    preview_id bigint references images (id)
);