CREATE TABLE IF NOT EXISTS line
(
    line_id bigint auto_increment not null,
    name varchar(255) not null unique,
    color varchar(20) not null,
    primary key(line_id)
);


CREATE TABLE IF NOT EXISTS station
(
    station_id BIGINT AUTO_INCREMENT NOT NULL,
    name varchar(255) NOT NULL,
    next_distance INT,
    previous_distance INT,
    line_id BIGINT,
    FOREIGN KEY (line_id) REFERENCES line(line_id) ON DELETE CASCADE,
    PRIMARY KEY(station_id)
);
