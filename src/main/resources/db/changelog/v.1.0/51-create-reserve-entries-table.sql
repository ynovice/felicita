create table reserve_entries
(
    id             bigserial primary key,
    price_per_item integer not null,
    item_id        bigint references items (id) not null,
    reserve_id     bigint references reserves (id) not null
);