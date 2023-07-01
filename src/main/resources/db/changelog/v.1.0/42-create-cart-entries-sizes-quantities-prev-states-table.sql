create table cart_entries_sizes_quantities_prev_states
(
    cart_entry_id bigint references cart_entries (id) not null,
    quantity      int not null,
    size_id       bigint references sizes (id) not null
);

--go