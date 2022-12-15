drop table if exists code_sources;

create table if not exists public.code_sources
(
    id                 bigserial primary key,
    source_in_zip      bytea not null,
    compilation_status varchar(25) default 'N/A',
    tests_number       int default 0,
    tests_passed       int default 0,
    testing_status     varchar(25) default 'N/A'
);
