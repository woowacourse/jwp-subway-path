DROP TABLE IF EXISTS SECTION;
DROP TABLE IF EXISTS STATION;
DROP TABLE IF EXISTS LINE;

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
    up_station_id       bigint                not null,
    down_station_id     bigint                not null,
    distance            int                   not null default 0,
    `order`             bigint                not null,
    primary key (id),
    foreign key (line_id) references LINE (id),
    foreign key (up_station_id) references STATION (id),
    foreign key (down_station_id) references STATION (id)
);

INSERT INTO LINE(name, color) VALUES ('1호선', 'bg-blue-500');
INSERT INTO LINE(name, color) VALUES ('2호선', 'bg-green-500');
INSERT INTO LINE(name, color) VALUES ('신분당선', 'bg-red-500');

INSERT INTO STATION(name) VALUES ('서울역');
INSERT INTO STATION(name) VALUES ('천안역');

INSERT INTO STATION(name) VALUES ('강남역');
INSERT INTO STATION(name) VALUES ('선릉역');
INSERT INTO STATION(name) VALUES ('잠실역');

INSERT INTO STATION(name) VALUES ('신논현역');
INSERT INTO STATION(name) VALUES ('판교역');

INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance, `order`) VALUES (1, 1, 2, 4, 1);

INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance, `order`) VALUES (2, 3, 4, 5, 1);
INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance, `order`) VALUES (2, 4, 5, 4, 2);
