CREATE TABLE IF NOT EXISTS LINE
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    name  VARCHAR(255)          NOT NULL UNIQUE,
    color VARCHAR(20)           NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS STATION
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    name    VARCHAR(255)          NOT NULL,
    line_id BIGINT                NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (line_id) REFERENCES LINE (id)
);

CREATE TABLE IF NOT EXISTS SECTION
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    left_station_id  BIGINT                NOT NULL,
    right_station_id BIGINT                NOT NULL,
    line_id          BIGINT                NOT NULL,
    distance         INT                   NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (line_id) REFERENCES LINE (id)
);

-- INSERT INTO LINE(id, name, color)
-- values (1, '2호선', '#123456');
-- INSERT INTO STATION(id, name, line_id)
-- values (1, '잠실역', 1),
--        (2, '선릉역', 1);
-- INSERT INTO SECTION(id, left_station_id, right_station_id, line_id, distance)
-- values (1, 1, 2, 1, 10)