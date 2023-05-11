create table if not exists STATION
(
    id bigint auto_increment not null
        PRIMARY KEY,
    name varchar(255) not null unique,
    primary key(id)
);

create table if not exists LINE
(
    id bigint auto_increment not null
        PRIMARY KEY,
    name varchar(255) not null unique,
    color varchar(20) not null,
    primary key(id)
);

create table if not exists SECTION
(
    id bigint auto_increment not null
        PRIMARY KEY,
    up_station varchar(20) not null,
    down_station varchar(20) not null,
    distance int not null,
    line_id int not null,
    CONSTRAINT line_id
        foreign key (line_id) references LINE (id)
    );
