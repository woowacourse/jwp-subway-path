CREATE TABLE IF NOT EXISTS station
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS line
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS section
(
    id              BIGINT NOT NULL AUTO_INCREMENT,
    up_station_id   BIGINT NOT NULL,
    down_station_id BIGINT NOT NULL,
    line_id         BIGINT NOT NULL,
    distance        INT    NOT NULL,
    PRIMARY KEY (id)
);
