create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
);

create table if not exists STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    line_id bigint not null,
    primary key(id),
    FOREIGN KEY (line_id) REFERENCES LINE (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS SUBWAY_SECTION
(
    id bigint auto_increment not null,
    up_station_id bigint not null,
    down_station_id bigint not null,
    line_id bigint not null,
    distance int not null,
    primary key(id),
    FOREIGN KEY (line_id) REFERENCES LINE (id) ON DELETE CASCADE,
    FOREIGN KEY (up_station_id) REFERENCES STATION (id) ON DELETE CASCADE,
    FOREIGN KEY (down_station_id) REFERENCES STATION (id) ON DELETE CASCADE
);

INSERT INTO LINE (name) VALUES ('2호선');
INSERT INTO STATION (name, line_id) VALUES ('잠실역', 1);
INSERT INTO STATION (name, line_id) VALUES ('건대역', 1);
INSERT INTO STATION (name, line_id) VALUES ('강변역', 1);
INSERT INTO SUBWAY_SECTION (up_station_id, down_station_id, line_id, distance) VALUES (1, 3, 1, 10);
INSERT INTO SUBWAY_SECTION (up_station_id, down_station_id, line_id, distance) VALUES (3, 2, 1, 5);
