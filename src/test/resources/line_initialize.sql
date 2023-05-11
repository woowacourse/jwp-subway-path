DROP TABLE IF EXISTS SECTION;
DROP TABLE IF EXISTS STATION;
DROP TABLE IF EXISTS LINE;

create table STATION
(
    name varchar(255) not null unique
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
    start_station_name varchar(255)          not null,
    end_station_name   varchar(255)          not null,
    distance           int                   not null,
    primary key (id),
    foreign key (line_id) REFERENCES LINE (id)
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

INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(1, '잠실역', '삼성역', 10);
INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(1, '삼성역', '포항역', 10);
INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(1, '포항역', '대구역', 10);

INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(2, '잠실역', '양재역', 10);
INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(2, '양재역', '대치역', 10);
INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(2, '대치역', '강남역', 10);
INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(2, '강남역', '상도역', 10);

INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(3, '장승배기역', '상도역', 10);
INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(3, '상도역', '숭실대입구역', 10);
INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(3, '숭실대입구역', '대치역', 10);
INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(3, '대치역', '삼성역', 10);
INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(3, '삼성역', '잠실역', 10);
