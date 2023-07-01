create table items
(
    id          bigserial primary key,
    active      boolean,
    created_at  timestamp(6) not null,
    description varchar(1000),
    has_print   boolean,
    name        varchar(100) not null,
    price       integer not null
);

--go
