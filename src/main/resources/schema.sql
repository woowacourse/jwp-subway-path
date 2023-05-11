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

INSERT INTO LINE(name, color) VALUES ('1호선', 'blue');
INSERT INTO LINE(name, color) VALUES ('2호선', 'green');
INSERT INTO LINE(name, color) VALUES ('3호선', 'pink');

INSERT INTO STATION(name) VALUES ('서울역');
INSERT INTO STATION(name) VALUES ('천안역');

INSERT INTO STATION(name) VALUES ('잠실역');
INSERT INTO STATION(name) VALUES ('선릉역');
INSERT INTO STATION(name) VALUES ('강남역');

INSERT INTO STATION(name) VALUES ('해운대역');
INSERT INTO STATION(name) VALUES ('동대구역');
INSERT INTO STATION(name) VALUES ('신천역');

INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance) VALUES (1, null, 1, 3);
INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance) VALUES (1, 1, 2, 4);
INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance) VALUES (1, 2, null, 4);

INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance) VALUES (2, null, 3, 3);
INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance) VALUES (2, 3, 4, 4);
INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance) VALUES (2, 4, 5, 4);
INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance) VALUES (2, 5, null, 4);
