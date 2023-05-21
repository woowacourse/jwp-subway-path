DROP TABLE IF EXISTS GENERAL_SECTION;
DROP TABLE IF EXISTS station;
DROP TABLE IF EXISTS line;

create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
);

create table if not exists STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null,
    line_id bigint not null,
    primary key(id)
);

CREATE TABLE IF NOT EXISTS GENERAL_SECTION
(
    id bigint auto_increment not null,
    up_station_id bigint not null,
    down_station_id bigint not null,
    line_id bigint not null,
    distance int not null,
    primary key(id)
);