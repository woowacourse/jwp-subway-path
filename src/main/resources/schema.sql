CREATE TABLE IF NOT EXISTS line
(
    line_id    BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255)          NOT NULL UNIQUE,
    color      VARCHAR(20)           NOT NULL,
    extra_fare INT                   NULL DEFAULT 0,
    PRIMARY KEY (line_id)
);


CREATE TABLE IF NOT EXISTS station
(
    station_id BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255)          NOT NULL UNIQUE,
    PRIMARY KEY (station_id)
);

CREATE TABLE IF NOT EXISTS section
(
    section_id        BIGINT AUTO_INCREMENT NOT NULL,
    first_station_id  BIGINT                NOT NULL,
    second_station_id BIGINT                NOT NULL,
    distance          INT,
    line_id           BIGINT,
    FOREIGN KEY (line_id) REFERENCES line (line_id),
    FOREIGN KEY (first_station_id) REFERENCES station (station_id),
    FOREIGN KEY (second_station_id) REFERENCES station (station_id),
    PRIMARY KEY (section_id)
);
