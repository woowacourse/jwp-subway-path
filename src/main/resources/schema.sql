CREATE TABLE IF NOT EXISTS station
(
    id
    BIGINT
    AUTO_INCREMENT
    NOT
    NULL,
    name
    VARCHAR
(
    255
) NOT NULL UNIQUE,
    PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS line
(
    id
    BIGINT
    AUTO_INCREMENT
    NOT
    NULL,
    name
    VARCHAR
(
    255
) NOT NULL UNIQUE,
    color VARCHAR
(
    20
) NOT NULL,
    up_endpoint_id BIGINT,
    down_endpoint_id BIGINT,
    PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS section
(
    id
    BIGINT
    AUTO_INCREMENT
    NOT
    NULL,
    departure_id
    BIGINT
    NOT
    NULL,
    arrival_id
    BIGINT
    NOT
    NULL,
    distance
    INT
    NOT
    NULL,
    line_id
    BIGINT
    NOT
    NULL,
    PRIMARY
    KEY
(
    id
)
    );
