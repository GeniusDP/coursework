drop table if exists submissions, tasks,
    pmd_violation, pmd_files, pmd_report,
    checkstyle_report, checkstyle_error, checkstyle_file;

create table if not exists public.tasks
(
    id                          bigserial primary key,
    name                        text    not null,
    description                 text    not null,
    public_sources_in_zip       bytea   not null,
    private_test_sources_in_zip bytea   not null,
    pmd_needed                  boolean not null,
    checkstyle_needed           boolean not null,
    pmd_points                  int     not null,
    checkstyle_points           int     not null,
    test_points                 int     not null
);

---------------------------------------------------------

create table if not exists public.pmd_reports
(
    id bigserial primary key
);


create table if not exists public.pmd_files
(
    id            bigserial primary key,
    name          varchar(300) not null,
    pmd_report_id bigint references public.pmd_reports (id)
);


create table if not exists public.pmd_violations
(
    id           bigserial primary key,
    value        text not null,
    begin_line   int  not null,
    end_line     int  not null,
    rule_name    text not null,
    package_name text not null,
    class_name   text not null,
    method_name  text not null,
    file_id      bigint references public.pmd_files (id)
);

---------------------------------------------------------
create table if not exists public.checkstyle_reports
(
    id bigserial primary key
);


create table if not exists public.checkstyle_files
(
    id                   bigserial primary key,
    name                 varchar(230) not null,
    checkstyle_report_id bigint references public.checkstyle_reports (id)
);

create table if not exists public.checkstyle_errors
(
    id                 bigserial primary key,
    line               int not null ,
    message            text not null,
    source             text not null,
    checkstyle_file_id bigint references public.checkstyle_files (id)
);

---------------------------------------------------------

create table if not exists public.submissions
(
    id                   bigserial primary key,
    source_in_zip        bytea not null,

    compilation_status   varchar(25),

    runtime_status       varchar(25),
    total_score          int     default 0,

    testing_status       varchar(25),
    tests_run            integer default 0,
    tests_passed         integer default 0,

    pmd_report_id        bigint references public.pmd_reports (id),
    checkstyle_report_id bigint references public.checkstyle_reports (id),

    user_id              bigint,
    task_id              bigint references tasks (id)
);
