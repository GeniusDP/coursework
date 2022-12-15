drop table if exists submissions, tasks;

create table if not exists public.tasks
(
    id                          bigserial primary key,
    name                        text  not null,
    description                 text  not null,
    public_sources_in_zip       bytea not null,
    private_test_sources_in_zip bytea not null
);


create table if not exists public.submissions
(
    id                 bigserial primary key,
    source_in_zip      bytea not null,
    compilation_status varchar(25),
    tests_number       integer default 0,
    tests_passed       integer default 0,
    testing_status     varchar(25),
    user_id            bigint,
    task_id            bigint references tasks (id)
);
