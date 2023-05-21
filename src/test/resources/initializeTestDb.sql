DROP TABLE section;
DROP TABLE station;
DROP TABLE line;

create table if not exists STATION
(
    id   bigint auto_increment not null,
    name varchar(255)          not null unique,
    primary key (id)
);

create table if not exists LINE
(
    id    bigint auto_increment not null,
    name  varchar(255)          not null unique,
    color varchar(20)           not null unique,
    primary key (id)
);

create table if not exists SECTION
(
    id              bigint auto_increment not null,
    line_id         bigint                not null references line (id) on delete cascade,
    up_station_id   bigint                not null references station (id) on delete cascade,
    down_station_id bigint                not null references station (id) on delete cascade,
    distance        int                   not null,
    primary key (id)
);



INSERT INTO line (name, color)
values ('2호선', '초록색');
--1
INSERT INTO line (name, color)
values ('1호선', '파랑색');
--2


INSERT INTO station (name)
values ('서울대입구역');
--1
INSERT INTO station (name)
values ('봉천역');
--2
INSERT INTO station (name)
values ('낙성대역');
--3
INSERT INTO station (name)
values ('사당역');
--4
INSERT INTO station (name)
values ('방배역');
--5
INSERT INTO station (name)
values ('교대역');
--6
INSERT INTO station (name)
values ('인천역');
--7
INSERT INTO station (name)
values ('동인천역');
--8

INSERT INTO section (line_id, up_station_id, down_station_id, distance)
values (1, 2, 1, 5);
INSERT INTO section (line_id, up_station_id, down_station_id, distance)
values (1, 1, 4, 7);
