CREATE TABLE IF NOT EXISTS line
(
    id                  BIGINT AUTO_INCREMENT  NOT NULL,
    name                VARCHAR(255)           NOT NULL,
    upward_terminus     VARCHAR(255)           NOT NULL,
    downward_terminus   VARCHAR(255)           NOT NULL,
    created_at          TIMESTAMP              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS section
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    line_id             BIGINT AUTO_INCREMENT NOT NULL,
    upward_station      VARCHAR(255)          NOT NULL,
    downward_station    VARCHAR(255)          NOT NULL,
    distance            INT                   NOT NULL,
    created_at          TIMESTAMP             NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (line_id) REFERENCES LINE (id) ON DELETE CASCADE
);
