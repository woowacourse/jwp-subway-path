create table if not exists STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
);

create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    extraFare bigint not null,
    primary key(id)
);

create table if not exists SECTION
(
    id bigint auto_increment not null,
    line_id bigint not null,
    left_station_id bigint not null,
    right_station_id bigint not null,
    distance bigint not null,

    primary key(id),
    foreign key (line_id) references line (id),
    foreign key (left_station_id) references station (id),
    foreign key (right_station_id) references station (id)
);
