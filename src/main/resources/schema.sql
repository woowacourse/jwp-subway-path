create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    color varchar(20) not null unique,
    primary key(id)
);

create table if not exists STATION
(
    id          bigint            not null      auto_increment ,
    line_id     bigint            not null      references line(id)        on delete cascade,
    name        varchar(255)      not null,
    primary key(id),
    unique (line_id, name)
);


create table if not exists SECTION
(
    id                 bigint    not null      auto_increment,
    line_id            bigint    not null      references line(id)        on delete cascade,
    up_station_id      bigint    not null      references station(id)     on delete cascade,
    down_station_id    bigint    not null      references station(id)     on delete cascade,
    distance           int       not null,
    primary key(id),
    unique(up_station_id, down_station_id)
);
