create table colors
(
    id   bigserial primary key,
    name varchar(30) unique not null
);

insert into colors (name)
    values ('чёрный'),
           ('белый'),
           ('зелёный'),
           ('жёлтый'),
           ('розовый'),
           ('красный'),
           ('бежевый'),
           ('коричневый'),
           ('оранжевый'),
           ('фиолетовый'),
           ('серый'),
           ('хаки'),
           ('голубой');