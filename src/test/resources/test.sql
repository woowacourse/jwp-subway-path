CREATE TABLE IF NOT EXISTS station
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS line
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    color VARCHAR(20) NOT NULL,
    PRIMARY KEY(id)
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

