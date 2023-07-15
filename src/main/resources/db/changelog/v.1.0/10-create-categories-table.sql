create table categories
(
    id        bigserial primary key,
    name      varchar(30) not null,
    parent_id bigint references categories (id) null
);

insert into categories (name)
    values ('Мужское'),   -- id 1
           ('Женское'),   -- id 2
           ('Футболки');  -- id 3

insert into categories (name, parent_id)
    values ('Жилеты', 2),
           ('Платья', 2),
           ('Футболки', 2),
           ('Шапки и шарфы', 2),
           ('Свитшоты', 2),
           ('Шорты', 2),
           ('Шорты', 1),
           ('Брюки', 1),
           ('Футболки', 1),
           ('Термобельё', 1);