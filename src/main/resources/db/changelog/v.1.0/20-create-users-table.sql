create table users
(
    id       bigserial primary key,
    role     varchar(40) not null,
    username varchar(60) not null
);