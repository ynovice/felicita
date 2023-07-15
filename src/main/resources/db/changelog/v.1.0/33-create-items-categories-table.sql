create table items_categories
(
    item_id     bigint references items (id) not null,
    category_id bigint references categories (id) not null
);