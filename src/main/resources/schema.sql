CREATE TABLE IF NOT EXISTS station
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)          NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS line
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    name           VARCHAR(255)          NOT NULL UNIQUE,
    color          VARCHAR(20)           NOT NULL,
    additionalFare INT                   NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS path
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    line_id         BIGINT                NOT NULL,
    up_station_id   BIGINT,
    distance        INT                   NOT NULL,
    down_station_id BIGINT,
    PRIMARY KEY (id)
)
