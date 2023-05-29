create table if not exists STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null,
    primary key(id)
);

create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    color varchar(20) not null,
    primary key(id)
);

create table if not exists SECTION
(
    id bigint not null auto_increment,
    line_id bigint not null,
    station_id bigint not null,
    next_station_id bigint,
    distance int not null,
    primary key (id),
    constraint not_same_station check (station_id != next_station_id)
)
