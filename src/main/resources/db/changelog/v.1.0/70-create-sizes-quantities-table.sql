create table sizes_quantities
(
    id               bigserial primary key,
    quantity         integer not null,
    cart_entry_id    bigint references cart_entries (id),
    item_id          bigint references items (id),
    reserve_entry_id bigint references reserve_entries (id),
    size_id          bigint not null references sizes (id)
);

--go