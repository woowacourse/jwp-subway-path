create table if not exists STATION
(
    id int unsigned auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
);

create table if not exists LINE
(
    id int unsigned auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
);

create table if not exists `SECTION`
(
    id int unsigned auto_increment not null,
    line_id int unsigned not null,
    start_station_id int unsigned not null,
    end_station_id int unsigned not null,
    distance int unsigned not null,
    primary key(id),
    FOREIGN KEY (start_station_id) REFERENCES STATION (id),
    FOREIGN KEY (end_station_id) REFERENCES STATION (id),
    FOREIGN KEY (line_id) REFERENCES LINE (id) ON DELETE CASCADE
);

ALTER TABLE `section`
ADD CONSTRAINT different_stations
CHECK (start_station_id <> end_station_id);
