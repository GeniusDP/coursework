drop table if exists code_sources;

create table if not exists public.code_sources
(
    id                 bigserial primary key,
    source_in_zip      bytea not null,
    compilation_status varchar(25),
    tests_number       integer default 0,
    tests_passed       integer default 0,
    testing_status     varchar(25)
);
