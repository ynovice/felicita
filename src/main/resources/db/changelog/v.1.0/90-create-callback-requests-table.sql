create table callback_requests
(
    id         bigserial primary key,
    created_at timestamp(6),
    name       varchar(255),
    phone      varchar(255)
);

--go