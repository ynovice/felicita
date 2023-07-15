create table sizes
(
    id   bigserial primary key,
    name varchar(30) unique not null
);

insert into sizes (name)
    values ('XXS'),
           ('XS'),
           ('S'),
           ('M'),
           ('L'),
           ('XL'),
           ('XXL'),
           ('XXXL');