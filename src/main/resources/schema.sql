create table if not exists station
(
    id   bigint auto_increment not null,
    station_name varchar(255)          not null,
    primary key (id)
);

create table if not exists line
(
    id   bigint auto_increment not null,
    line_name varchar(255)          not null unique,
    primary key (id)
);

create table if not exists section
(
    id              bigint auto_increment not null,
    line_id         bigint                not null,
    up_station_id   bigint                not null,
    down_station_id bigint                not null,
    distance        int                   not null,
    primary key (id),
    FOREIGN KEY (line_id) REFERENCES LINE (id),
    FOREIGN KEY (up_station_id) REFERENCES STATION (id),
    FOREIGN KEY (down_station_id) REFERENCES STATION (id)
);
