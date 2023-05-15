DROP TABLE station;
DROP TABLE line;
DROP TABLE section;

CREATE TABLE IF NOT EXISTS station
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
);

CREATE TABLE IF NOT EXISTS line
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    color varchar(20) not null,
    primary key(id)
);

CREATE TABLE IF NOT EXISTS section
(
    id bigint auto_increment not null,
    line_id bigint not null,
    distance int not null,
    previous_station_id bigint not null,
    next_station_id bigint not null,
    primary key(id)
);

INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');

INSERT INTO line(name, color)
VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');

INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
