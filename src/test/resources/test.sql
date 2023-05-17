create table if not exists STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
);

create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    color varchar(20) not null,
    primary key(id)
);

CREATE TABLE IF NOT EXISTS paths
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    line_id BIGINT NOT NULL,
    up_station_id BIGINT NOT NULL,
    down_station_id BIGINT NOT NULL,
    distance INT NOT NULL,
    PRIMARY KEY(id)
);
INSERT INTO line (name, color)
VALUES ('1호선', '파랑'),
       ('2호선', '초록'),
       ('empty', 'none');

INSERT INTO station (name)
VALUES ('수원'),
       ('잠실나루'),
       ('의왕'),
       ('선릉'),
       ('여긴 못감');

INSERT INTO paths (line_id, up_station_id, down_station_id, distance)
VALUES (1, 1, 2, 5),
       (2, 3, 1, 5),
       (2, 1, 4, 7);

