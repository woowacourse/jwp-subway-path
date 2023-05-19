create table if not exists line
(
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(20) NOT NULL,
    color  VARCHAR(7)  NOT NULL
);

create table if not exists station
(
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(20) NOT NULL
);

create table if not exists station_edge
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    line_id         BIGINT NOT NULL,
    up_station_id   BIGINT NOT NULL,
    down_station_id BIGINT NOT NULL,
    distance        INT    NOT NULL,
    FOREIGN KEY (line_id) REFERENCES line (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (up_station_id) REFERENCES station (id),
    FOREIGN KEY (down_station_id) REFERENCES station (id)
);
