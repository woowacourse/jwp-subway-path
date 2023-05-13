DROP TABLE if exists SUBWAY_MAP;
DROP TABLE if exists STATION;
DROP TABLE if exists LINE;

create table if not exists STATION
(
    id   bigint auto_increment not null,
    name varchar(255)          not null unique,
    primary key (id)
);

create table if not exists LINE
(
    id              bigint auto_increment not null,
    name            varchar(255)          not null unique,
    color           varchar(20)           not null,
    head_station_id bigint,
    primary key (id),
    foreign key (head_station_id) references STATION (id)
);

create table if not exists SUBWAY_MAP
(
    id                 bigint auto_increment not null,
    line_id            bigint                not null,
    current_station_id bigint                not null,
    next_station_id    bigint,
    distance           int                   not null,
    foreign key (line_id) references LINE (id),
    foreign key (current_station_id) references STATION (id),
    foreign key (next_station_id) references STATION (id),
    primary key (id)
);
