DROP TABLE LINE IF EXISTS;
DROP TABLE STATION IF EXISTS;

create table STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null,
    next_station bigint null,
    distance bigint null,
    line_id bigint,
    primary key(id)
);

create table LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    color varchar(20) not null,
    extra_charge int not null default 0,
    head_station bigint,
    primary key(id)
);
