create table images
(
    id        bigserial primary key,
    extension varchar(10) not null,
    item_id   bigint references items (id)
);

--go