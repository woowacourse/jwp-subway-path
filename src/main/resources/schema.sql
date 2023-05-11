create table if not exists STATION
(
    id   bigint auto_increment not null,
    name varchar(255)          not null unique,
    primary key (id)
    );

create table if not exists LINE
(
    id    bigint auto_increment not null,
    name  varchar(255)          not null unique,
    color varchar(20)           not null unique,
    primary key (id)
    );

create table if not exists INTERSTATION
(
    id               bigint auto_increment not null,
    line_id          bigint                not null,
    distance         bigint                not null,
    start_station_id bigint                not null,
    end_station_id   bigint                not null,
    primary key (id),
    foreign key (line_id) references LINE (id),
    foreign key (start_station_id) references STATION (id),
    foreign key (end_station_id) references STATION (id)
    );
