CREATE TABLE IF NOT EXISTS line
(
    id bigint auto_increment not null,
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
    first_station  varchar(255)          NOT NULL,
    second_station varchar(255)          NOT NULL,
    distance       INT,
    line_id        BIGINT,
    FOREIGN KEY (line_id) REFERENCES line (id),
    FOREIGN KEY (first_station) REFERENCES station (name),
    FOREIGN KEY (second_station) REFERENCES station (name),
    PRIMARY KEY (id)
);
