create table if not exists station
(
    id   bigint not null auto_increment,
    name varchar(255)          not null unique,
    primary key (id)
);

create table if not exists line
(
    id    bigint not null auto_increment,
    name  varchar(255)          not null unique ,
    color varchar(20)           not null,
    primary key (id)
);

create table if not exists section
(
    id              bigint not null auto_increment,
    distance        int                   not null,
    up_station_id   bigint                not null,
    down_station_id bigint                not null,
    line_id         bigint                not null,
    primary key (id)
);

truncate table section restart identity;
truncate table station restart identity;
truncate table line restart identity;

INSERT INTO line(name, color) VALUES('2호선', '초록');
INSERT INTO line(name, color) VALUES('8호선', '파랑');
INSERT INTO station(name) VALUES('잠실새내');
INSERT INTO station(name) VALUES('잠실');
INSERT INTO station(name) VALUES('잠실나루');
INSERT INTO station(name) VALUES('몽촌토성');
INSERT INTO station(name) VALUES('석촌');
INSERT INTO station(name) VALUES('더미');
INSERT INTO section(distance, up_station_id, down_station_id, line_id)VALUES (10, 1, 2, 1);
INSERT INTO section(distance, up_station_id, down_station_id, line_id) VALUES (15, 2, 3, 1);
INSERT INTO section(distance, up_station_id, down_station_id, line_id)VALUES (10, 4, 3, 2);
INSERT INTO section(distance, up_station_id, down_station_id, line_id) VALUES (15, 3, 5, 2);

