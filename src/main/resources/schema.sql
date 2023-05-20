create table if not exists STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null,
    next_station bigint,
    distance int,
    line_id bigint,
    primary key(id)
);

create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    color varchar(20) not null,
    head_station bigint,
    primary key(id)
);
