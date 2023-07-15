create table items_colors
(
    item_id  bigint references items (id) not null,
    color_id bigint references colors (id) not null
);