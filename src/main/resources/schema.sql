CREATE TABLE IF NOT EXISTS line
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    name    varchar(255)          not null UNIQUE,
    color   varchar(20)           not null,
    extra_charge    BIGINT           NOT NULL,
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
    PRIMARY KEY (id)
);
