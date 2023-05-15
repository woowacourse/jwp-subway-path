-- TODO: Foreign key 등록하기
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
    color varchar(20) not null,
    upbound_station_id bigint,
    downbound_station_id bigint,
    primary key(id)
);

create table if not exists LINE_STATION
(
    id bigint auto_increment not null,
    station_id bigint not null,
    line_id bigint not null,
    primary key(id)
);

create table if not exists SECTION
(
    id bigint auto_increment not null,
    line_id bigint not null,
    left_station_id bigint not null,
    right_station_id bigint not null,
    distance int not null,
    primary key(id)
);
