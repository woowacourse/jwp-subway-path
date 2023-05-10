CREATE TABLE IF NOT EXISTS line
(
    line_id bigint auto_increment not null,
    name    varchar(255)          not null UNIQUE,
    color   varchar(20)           not null,
    primary key (line_id)
);


CREATE TABLE IF NOT EXISTS station
(
    station_id BIGINT AUTO_INCREMENT NOT NULL,
    name       varchar(255)          NOT NULL UNIQUE,
    PRIMARY KEY (station_id)
);

CREATE TABLE IF NOT EXISTS section
(
    section_id     BIGINT AUTO_INCREMENT NOT NULL,
    first_station  varchar(255)          NOT NULL,
    second_station varchar(255)          NOT NULL,
    distance       INT,
    line_id        BIGINT,
    FOREIGN KEY (line_id) REFERENCES line (line_id) ON DELETE CASCADE,
    FOREIGN KEY (first_station) REFERENCES station (name) ON DELETE CASCADE,
    FOREIGN KEY (second_station) REFERENCES station (name) ON DELETE CASCADE,
    PRIMARY KEY (section_id)
);
