DROP TABLE if exists STATION;
DROP TABLE if exists LINE;
DROP TABLE if exists SECTIONS;

create table STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
);

create table LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    color varchar(20) not null,
    primary key(id)
);

create table SECTIONS
(
    up_id bigint not null,
    down_id bigint not null,
    line_id bigint not null,
    distance int not null
);
