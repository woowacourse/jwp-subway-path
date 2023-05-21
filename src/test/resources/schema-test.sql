DROP TABLE  if exists section;
DROP TABLE  if exists station;
DROP TABLE  if exists line;

CREATE TABLE IF NOT EXISTS station
(
    id bigint auto_increment not null PRIMARY KEY,
    name varchar(255) not null unique
    );

CREATE TABLE IF NOT EXISTS line
(
    id bigint auto_increment not null PRIMARY KEY,
    name varchar(255) not null unique,
    color varchar(20) not null
    );

CREATE TABLE IF NOT EXISTS section
(
    id bigint auto_increment not null PRIMARY KEY,
    distance int not null,
    up_station_id int not null,
    down_station_id int not null,
    line_id int not null,

--     CONSTRAINT line_id
--     foreign key (line_id) references LINE (id) ON DELETE CASCADE,
--     CONSTRAINT up_station_id
--     foreign key (up_station_id) references STATION (id) ON DELETE CASCADE,
--     CONSTRAINT down_station_id
--     foreign key (down_station_id) references STATION (id) ON DELETE CASCADE,
    CONSTRAINT unique_stations
    UNIQUE (up_station_id, down_station_id)
    );
