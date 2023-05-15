create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
);

create table if not exists SECTION
(
    id bigint auto_increment not null,
    current_station_name varchar(255) not null,
    next_station_name varchar(255) not null,
    distance int not null,
    line_id bigint not null,
    primary key(id),
    unique key(current_station_name, next_station_name)
);
