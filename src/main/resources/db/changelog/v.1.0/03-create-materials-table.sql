create table materials
(
    id   bigserial primary key,
    name varchar(30) unique not null
);

insert into materials (name)
    values ('лён'),
           ('полиэстр'),
           ('хлопок'),
           ('шёлк'),
           ('шерсть');