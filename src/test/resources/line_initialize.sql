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
    distance           double                   not null,
    primary key (id),
    foreign key (line_id) REFERENCES LINE (id),
    foreign key (start_station_id) REFERENCES STATION (id),
    foreign key (end_station_id) REFERENCES STATION (id)
);

INSERT INTO STATION (name) VALUES ('교대역');
INSERT INTO STATION (name) VALUES ('서초역');
INSERT INTO STATION (name) VALUES ('방배역');
INSERT INTO STATION (name) VALUES ('사당역');
INSERT INTO STATION (name) VALUES ('이수역');
INSERT INTO STATION (name) VALUES ('동작역');
INSERT INTO STATION (name) VALUES ('남부터미널역');
INSERT INTO STATION (name) VALUES ('양재역');
INSERT INTO LINE (name, color) VALUES ('2호선', 'green');
INSERT INTO LINE (name, color) VALUES ('3호선', 'orange');
INSERT INTO LINE (name, color) VALUES ('4호선', 'blue');

INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(1, 4, 3, 10);
INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(1, 3, 2, 10);
INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(1, 2, 1, 10);

INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(2, 8, 7, 10);
INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(2, 7, 1, 10);

INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(3, 4, 5, 10);
INSERT INTO SECTION(line_id, start_station_id, end_station_id, distance) VALUES(3, 5, 6, 10);
