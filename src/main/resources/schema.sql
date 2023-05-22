CREATE SCHEMA IF NOT EXISTS subway;

DROP TABLE IF EXISTS section;
DROP TABLE IF EXISTS station;
DROP TABLE IF EXISTS line;

CREATE TABLE IF NOT EXISTS station
(
    stationId BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255)          NOT NULL UNIQUE,
    primary key (stationId)
);

CREATE TABLE IF NOT EXISTS line
(
    lineId     BIGINT AUTO_INCREMENT NOT NULL,
    name        VARCHAR(255)          NOT NULL UNIQUE,
    PRIMARY KEY (lineId)
);

CREATE TABLE IF NOT EXISTS section
(
    sectionId      BIGINT AUTO_INCREMENT NOT NULL,
    lineId         BIGINT                NOT NULL,
    upStationId   BIGINT                NOT NULL,
    downStationId BIGINT                NOT NULL,
    distance        BIGINT                NOT NULL,
    PRIMARY KEY (sectionId),
    FOREIGN KEY (lineId) REFERENCES line (lineId),
    FOREIGN KEY (upStationId) REFERENCES station (stationId),
    FOREIGN KEY (downStationId) REFERENCES station (stationId)
);
