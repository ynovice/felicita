create table items_materials
(
    item_id     bigint references items (id) not null,
    material_id bigint references materials (id) not null
);

--go