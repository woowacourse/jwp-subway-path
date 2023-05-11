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
    primary key(id)
    );

create table if not exists sections
(
    id bigint auto_increment not null,
    line_id bigint not null,
    departure_id bigint not null,
    arrival_id bigint not null,
    distance int not null,
    primary key(id)
    );
