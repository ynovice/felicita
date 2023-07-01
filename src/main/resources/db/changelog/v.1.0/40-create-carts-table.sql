create table carts
(
    id          bigserial primary key,
    total_items integer not null,
    total_price integer not null,
    user_id     bigint references users (id) unique not null
);

--go