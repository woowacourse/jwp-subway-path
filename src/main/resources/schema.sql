create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    surcharge int not null,
    primary key(id)
);

create table if not exists STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null,
    line_id bigint not null,
    primary key(id),
    FOREIGN KEY (line_id) REFERENCES LINE (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS SECTION
(
    id bigint auto_increment not null,
    up_station_id bigint not null,
    down_station_id bigint not null,
    distance int not null,
    line_id bigint not null,
    primary key(id),
    FOREIGN KEY (up_station_id) REFERENCES STATION (id) ON DELETE CASCADE,
    FOREIGN KEY (down_station_id) REFERENCES STATION (id) ON DELETE CASCADE,
    FOREIGN KEY (line_id) REFERENCES LINE (id) ON DELETE CASCADE
);
