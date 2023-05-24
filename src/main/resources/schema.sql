DROP TABLE IF EXISTS SECTION;
DROP TABLE IF EXISTS STATION;
DROP TABLE IF EXISTS LINE;

create table STATION
(
    name varchar(255) not null,
    primary key (name)
);

create table LINE
(
    id        bigint auto_increment not null,
    name      varchar(255)          not null,
    color     varchar(20)           not null,
    extra_fee bigint                not null,
    primary key (id)
);

create table SECTION
(
    id                 bigint auto_increment not null,
    line_id            bigint                not null,
    start_station_name varchar(255)          not null,
    end_station_name   varchar(255)          not null,
    distance           int                   not null,
    primary key (id),
    foreign key (line_id) REFERENCES LINE (id),
    foreign key (start_station_name) REFERENCES STATION (name)
        ON UPDATE CASCADE,
    foreign key (end_station_name) REFERENCES STATION (name)
        ON UPDATE CASCADE
);
