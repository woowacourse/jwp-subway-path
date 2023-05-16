CREATE TABLE IF NOT EXISTS line
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    name    varchar(255)          not null UNIQUE,
    color   varchar(20)           not null,
    primary key (id)
);


CREATE TABLE IF NOT EXISTS station
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    name       varchar(255)          NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS section
(
    id     BIGINT AUTO_INCREMENT NOT NULL,
    first_station_id  BIGINT          NOT NULL,
    second_station_id BIGINT          NOT NULL,
    distance       BIGINT,
    line_id        BIGINT,
    FOREIGN KEY (line_id) REFERENCES line (id) ON DELETE CASCADE,
    FOREIGN KEY (first_station_id) REFERENCES station (id) ON DELETE CASCADE,
    FOREIGN KEY (second_station_id) REFERENCES station (id) ON DELETE CASCADE,
    PRIMARY KEY (id)
);
