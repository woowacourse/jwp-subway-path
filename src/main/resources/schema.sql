create table if not exists STATION(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
);

create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    additional_fee int not null ,
    primary key(id)
);

create table if not exists SECTIONS
(
    id bigint auto_increment not null,
    up_station_id bigint not null,
    down_station_id bigint not null,
    distance int not null,
    line_id bigint not null,
    primary key(id),
    FOREIGN KEY (up_station_id) REFERENCES STATION(id),
    FOREIGN KEY (down_station_id) REFERENCES STATION(id),
    FOREIGN KEY (line_id) REFERENCES LINE(id)
);
