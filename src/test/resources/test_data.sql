truncate table STATION restart identity;
truncate table LINE restart identity;
truncate table SECTION restart identity;

create table if not exists STATION
(
    id   bigint auto_increment not null,
    name varchar(255)          not null unique,
    primary key(id)
);

create table if not exists LINE
(
    id    bigint auto_increment not null,
    name  varchar(255)          not null unique,
    color varchar(20)           not null,
    primary key(id)
);

create table if not exists SECTION
(
    id       bigint auto_increment not null,
    line_id  bigint                not null,
    from_id  bigint                not null,
    to_id    bigint                not null,
    distance bigint                not null,
    primary key(id)
);
