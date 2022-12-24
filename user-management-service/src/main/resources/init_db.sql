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
    refresh_token text,
    role_id       bigint             not null references roles (id)
);

insert into app_users (username, email, password, first_name, last_name, is_activated, refresh_token, role_id)
values ('student', 'stud@gmail.com', 'student', 'John', 'Doe', true, null, 1),
       ('teacher', 'teacher@gmail.com', 'teacher', 'Marco', 'Behler', true, null, 2),
       ('admin', 'admin@gmail.com', 'admin', 'admin', 'admin', true, null, 3);