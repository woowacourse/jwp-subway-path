DROP TABLE IF EXISTS section;
DROP TABLE IF EXISTS station;
DROP TABLE IF EXISTS line;

CREATE TABLE IF NOT EXISTS station
(
    station_id BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255)          NOT NULL UNIQUE,
    primary key (station_id)
);

CREATE TABLE IF NOT EXISTS line
(
    line_id     BIGINT AUTO_INCREMENT NOT NULL,
    line_number BIGINT                NOT NULL UNIQUE,
    name        VARCHAR(255)          NOT NULL UNIQUE,
    color       VARCHAR(20)           NOT NULL,
    PRIMARY KEY (line_id)
);

CREATE TABLE IF NOT EXISTS section
(
    section_id      BIGINT AUTO_INCREMENT NOT NULL,
    line_id         BIGINT                NOT NULL,
    up_station_id   BIGINT                NOT NULL,
    down_station_id BIGINT                NOT NULL,
    distance        BIGINT                NOT NULL,
    PRIMARY KEY (section_id),
    FOREIGN KEY (line_id) REFERENCES line (line_id),
    FOREIGN KEY (up_station_id) REFERENCES station (station_id),
    FOREIGN KEY (down_station_id) REFERENCES station (station_id)
);
