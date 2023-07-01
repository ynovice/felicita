create table cart_entries
(
    id      bigserial primary key,
    cart_id bigint references carts (id) not null,
    item_id bigint references items (id) not null,
    constraint cart_entry_unique_cart_id_item_id
        unique (cart_id, item_id)
);

--go