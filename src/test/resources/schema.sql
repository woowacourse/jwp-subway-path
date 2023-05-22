DROP TABLE IF EXISTS station;
DROP TABLE IF EXISTS line;
DROP TABLE IF EXISTS section;

CREATE TABLE IF NOT EXISTS station
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(50)           NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS line
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    name  VARCHAR(50)           NOT NULL UNIQUE,
    color VARCHAR(20)           NOT NULL,
    cost  INT                   NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS section
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    line_id         BIGINT                NOT NULL,
    up_station_id   BIGINT                NOT NULL,
    down_station_id BIGINT                NOT NULL,
    distance        INT                   NOT NULL,
    PRIMARY KEY (id)
);
