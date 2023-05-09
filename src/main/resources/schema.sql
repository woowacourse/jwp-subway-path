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

create table if not exists SECTION
(
    id                  bigint auto_increment not null,
    line_id             bigint                not null,
    up_station_id       bigint,
    down_station_id     bigint,
    distance            int                   not null default 0,
    primary key (id),
    foreign key (line_id) references LINE (id),
    foreign key (up_station_id) references STATION (id),
    foreign key (down_station_id) references STATION (id)
);

INSERT INTO LINE(name, color) VALUES ('2호선', 'green');
INSERT INTO STATION(name) VALUES ('잠실역');
INSERT INTO STATION(name) VALUES ('선릉역');

