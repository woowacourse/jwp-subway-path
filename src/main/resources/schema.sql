create table if not exists LINE
(
    id        bigint            not null      auto_increment,
    name      varchar(255)      not null,
    color     varchar(20)       not null,
    primary key(id),
    unique (name, color)
);

create table if not exists STATION
(
    id          bigint            not null      auto_increment,
    name        varchar(255)      not null      unique,
    primary key(id)
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
