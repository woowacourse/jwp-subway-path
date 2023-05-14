CREATE TABLE IF NOT EXISTS station
(
    station_id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)          NOT NULL UNIQUE,
    PRIMARY KEY (station_id)
);

CREATE TABLE IF NOT EXISTS line
(
    line_id               BIGINT AUTO_INCREMENT NOT NULL,
    name             VARCHAR(255)          NOT NULL UNIQUE,
    color            VARCHAR(20)           NOT NULL,
    PRIMARY KEY (line_id)
);

CREATE TABLE IF NOT EXISTS section
(
    section_id           BIGINT AUTO_INCREMENT NOT NULL,
    up_station_id BIGINT                NOT NULL,
    down_station_id   BIGINT                NOT NULL,
    distance     INT                   NOT NULL,
    line_id      BIGINT                NOT NULL,
    list_order  INT NOT NULL,
    PRIMARY KEY (section_id)
);


