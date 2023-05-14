DROP TABLE IF EXISTS STATION;

CREATE TABLE STATION
(
    id   BIGINT      NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS LINE;

CREATE TABLE LINE
(
    id   BIGINT      NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS EDGE;

CREATE TABLE EDGE
(
    id               BIGINT NOT NULL AUTO_INCREMENT,
    line_id          BIGINT NOT NULL,
    station_id       BIGINT NOT NULL,
    station_order    BIGINT NOT NULL,
    distance_to_next BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (station_id) REFERENCES STATION (id),
    FOREIGN KEY (line_id) REFERENCES LINE (id)
);

-- DROP TABLE IF EXISTS LINE;
--
-- CREATE TABLE LINE
-- (
--     id            BIGINT      NOT NULL AUTO_INCREMENT,
--     name          VARCHAR(20) NOT NULL,
--     station_id    BIGINT      NOT NULL,
--     station_order BIGINT      NOT NULL,
--     FOREIGN KEY (station_id) REFERENCES STATION (id),
--     PRIMARY KEY (id)
-- );
