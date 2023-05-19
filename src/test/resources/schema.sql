create table if not exists STATION
(
    id int auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
);

create table if not exists LINE
(
    id int auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
);

create table if not exists `SECTION`
(
    id int auto_increment not null,
    line_id int not null,
    start_station_id int not null,
    end_station_id int not null,
    distance int not null,
    primary key(id),
    FOREIGN KEY (start_station_id) REFERENCES STATION (id),
    FOREIGN KEY (end_station_id) REFERENCES STATION (id),
    FOREIGN KEY (line_id) REFERENCES LINE (id) ON DELETE CASCADE
);
