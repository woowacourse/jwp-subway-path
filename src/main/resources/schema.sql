DROP TABLE IF EXISTS section;
DROP TABLE IF EXISTS station_point;
DROP TABLE IF EXISTS station_in_line;
DROP TABLE IF EXISTS station;
DROP TABLE IF EXISTS line;

CREATE TABLE IF NOT EXISTS station
(
    station_id BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255)          NOT NULL UNIQUE,
    primary key (station_id)
);

INSERT INTO station(station_id, name)
VALUES (1, '2호선'),
       (2, '8호선');

CREATE TABLE IF NOT EXISTS line
(
    line_id BIGINT AUTO_INCREMENT NOT NULL,
    name    VARCHAR(255)          NOT NULL UNIQUE,
    color   VARCHAR(20)           NOT NULL,
    PRIMARY KEY (line_id)
);

INSERT INTO line(line_id, name, color)
VALUES (1, '잠실역', '초록색'),
       (2, '잠실새내역', '초록색');

CREATE TABLE IF NOT EXISTS section
(
    section_id      BIGINT AUTO_INCREMENT NOT NULL,
    up_station_id   BIGINT                NOT NULL,
    down_station_id BIGINT                NOT NULL,
    distance        BIGINT                NOT NULL,
    PRIMARY KEY (section_id),
    FOREIGN KEY (up_station_id) REFERENCES station (station_id),
    FOREIGN KEY (down_station_id) REFERENCES station (station_id)
);

CREATE TABLE IF NOT EXISTS station_end_point
(
    station_point_id BIGINT AUTO_INCREMENT NOT NULL,
    line_id          BIGINT                NOT NULL,
    up_station_id    BIGINT                NOT NULL,
    down_station_id  BIGINT                NOT NULL,
    PRIMARY KEY (station_point_id),
    FOREIGN KEY (line_id) REFERENCES line (line_id),
    FOREIGN KEY (up_station_id) REFERENCES station (station_id),
    FOREIGN KEY (down_station_id) REFERENCES station (station_id)
);

CREATE TABLE IF NOT EXISTS station_in_line
(
    station_in_line BIGINT AUTO_INCREMENT NOT NULL,
    station_id      BIGINT                NOT NULL,
    line_id         BIGINT                NOT NULL,
    PRIMARY KEY (station_in_line),
    FOREIGN KEY (station_id) REFERENCES station (station_id),
    FOREIGN KEY (line_id) REFERENCES line (line_id)
);
