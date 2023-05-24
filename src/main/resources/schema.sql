DROP TABLE IF EXISTS station;
DROP TABLE IF EXISTS section;
DROP TABLE IF EXISTS transfer;
DROP TABLE IF EXISTS line;

CREATE TABLE line
(
    id                  BIGINT AUTO_INCREMENT  NOT NULL,
    name                VARCHAR(255)           NOT NULL,
    surcharge           BIGINT                 NOT NULL,
    created_at          TIMESTAMP              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE station
(
    id                  BIGINT AUTO_INCREMENT  NOT NULL,
    line_id             BIGINT                 NOT NULL,
    name                VARCHAR(255)           NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (line_id) REFERENCES LINE (id) ON DELETE CASCADE
);

CREATE TABLE section
(
    id                  BIGINT AUTO_INCREMENT  NOT NULL,
    line_id             BIGINT                 NOT NULL,
    upward_station_id   BIGINT                 NOT NULL,
    downward_station_id BIGINT                 NOT NULL,
    distance            INT                    NOT NULL,
    created_at          TIMESTAMP              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (line_id) REFERENCES LINE (id) ON DELETE CASCADE
);

CREATE TABLE transfer
(
    id                  BIGINT AUTO_INCREMENT  NOT NULL,
    first_station_id    BIGINT                 NOT NULL,
    last_station_id     BIGINT                 NOT NULL,
    created_at          TIMESTAMP              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
