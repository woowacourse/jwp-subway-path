create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
);

create table if not exists SECTION
(
    id bigint auto_increment not null,
    next_section_id bigint,
    starter_yn varchar(1) not null,
    current_station_name varchar(255) not null,
    next_station_name varchar(255) not null,
    distance int not null,
    line_id bigint not null,
    primary key(id)
);

create table if not exist STATION
(
    id bigint auto_increment not null,
    from_station varchar(255) not null,
    to_station varchar(255) not null,
    section_id bigint not null,
    primary key(id)
)
