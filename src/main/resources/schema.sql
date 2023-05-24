CREATE TABLE IF NOT EXISTS station
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS line
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    extra_charge INT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS section
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    line_id        BIGINT NOT NULL,
    upstation_id   BIGINT NOT NULL,
    downstation_id BIGINT NOT NULL,
    distance       INT    NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (line_id) REFERENCES line (id) ON DELETE CASCADE,
    FOREIGN KEY (upstation_id) REFERENCES station (id) ON DELETE CASCADE,
    FOREIGN KEY (downstation_id) REFERENCES station (id) ON DELETE CASCADE
);
