CREATE TABLE STATION
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE LINE
(
    id    BIGINT       NOT NULL AUTO_INCREMENT,
    name  VARCHAR(255) NOT NULL UNIQUE,
    color VARCHAR(20)  NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE LINE_SECTION
(
    id              BIGINT  NOT NULL AUTO_INCREMENT,
    distance        INTEGER NOT NULL,
    up_station_id   BIGINT  NOT NULL,
    down_station_id BIGINT  NOT NULL,
    line_id         BIGINT  NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (up_station_id) REFERENCES STATION (id),
    FOREIGN KEY (down_station_id) REFERENCES STATION (id),
    FOREIGN KEY (line_id) REFERENCES LINE (id)
);
