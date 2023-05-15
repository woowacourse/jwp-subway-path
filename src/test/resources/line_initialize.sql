DROP TABLE IF EXISTS SECTION;
DROP TABLE IF EXISTS STATION;
DROP TABLE IF EXISTS LINE;

create table STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    primary key (id)
);

create table LINE
(
    id                 bigint auto_increment not null,
    name               varchar(255)          not null unique,
    color              varchar(20)           not null,
    primary key (id)
);

create table SECTION
(
    id                 bigint auto_increment not null,
    line_id            bigint                not null,
    start_station_id   bigint                not null,
    end_station_id     bigint                not null,
    distance           int                   not null,
    primary key (id),
    foreign key (line_id) REFERENCES LINE (id),
    foreign key (start_station_id) REFERENCES STATION (id),
    foreign key (end_station_id) REFERENCES STATION (id)
);

INSERT INTO STATION (name) VALUES ('잠실역');
INSERT INTO STATION (name) VALUES ('삼성역');
INSERT INTO STATION (name) VALUES ('포항역');
INSERT INTO STATION (name) VALUES ('대구역');
INSERT INTO STATION (name) VALUES ('강남역');
INSERT INTO STATION (name) VALUES ('대치역');
INSERT INTO STATION (name) VALUES ('양재역');
INSERT INTO STATION (name) VALUES ('장승배기역');
INSERT INTO STATION (name) VALUES ('상도역');
INSERT INTO STATION (name) VALUES ('숭실대입구역');
INSERT INTO LINE (name, color) VALUES ('2호선', 'green');
INSERT INTO LINE (name, color) VALUES ('3호선', 'orange');
INSERT INTO LINE (name, color) VALUES ('4호선', 'blue');

INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(1, 1, 2, 10);
INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(1, 2, 3, 10);
INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(1, 3, 4, 10);

INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(2, 1, 7, 10);
INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(2, 7, 6, 10);
INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(2, 6, 5, 10);
INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(2, 5, 9, 10);

INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(3, 8, 9, 10);
INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(3, 9, 10, 10);
INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(3, 10, 6, 10);
INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(3, 6, 2, 10);
INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(3, 2, 1, 10);
