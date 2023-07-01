create table public.reserves
(
    id          bigserial primary key,
    created_at  timestamp(6) not null,
    total_items integer not null,
    total_price integer not null,
    user_id     bigint references users (id)
);

--go