drop table if exists app_users, roles cascade ;

create table if not exists roles
(
    id   bigserial primary key,
    name varchar(10) unique not null
);

insert into roles (name)
values ('STUDENT'),
       ('TEACHER'),
       ('ADMIN');


create table if not exists app_users
(
    id            bigserial primary key,
    username      varchar(50) unique not null,
    email         varchar(50)        not null,
    password      varchar(50)        not null,
    first_name    varchar(50)        not null,
    last_name     varchar(50)        not null,
    is_activated  boolean            not null default false,
    refresh_token text,
    role_id       bigint             not null references roles (id)
);
