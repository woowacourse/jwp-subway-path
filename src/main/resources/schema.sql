create table if not exists station
(
    id   bigint       not null auto_increment,
    name varchar(255) not null unique,
    primary key (id)
);

create table if not exists line
(
    id    bigint       not null auto_increment,
    name  varchar(255) not null unique,
    color varchar(20)  not null,
    primary key (id)
);

create table if not exists section
(
    id              bigint  not null auto_increment,
    distance        int     not null,
    is_start        boolean not null,
    up_station_id   bigint  not null,
    down_station_id bigint  not null,
    line_id         bigint  not null,
    primary key (id)
);

create table if not exists line_expense
(
    id           bigint not null auto_increment,
    per_expense     int    not null,
    per_distance int    not null,
    line_id      bigint not null,
    primary key (id),
    unique (line_id)
);
