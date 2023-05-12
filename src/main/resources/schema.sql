DROP TABLE IF EXISTS station;
CREATE TABLE station
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS line;
CREATE TABLE line
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    name  VARCHAR(50) NOT NULL UNIQUE,
    color VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS section;
CREATE TABLE section
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    line_id         BIGINT NOT NULL,
    up_station_id   BIGINT NOT NULL,
    down_station_id BIGINT NOT NULL,
    distance        INT    NOT NULL,
    PRIMARY KEY (id)
);
