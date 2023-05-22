CREATE TABLE IF NOT EXISTS station
(
    id bigint auto_increment NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS line
(
    id bigint auto_increment NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    color VARCHAR(20) NOT NULL,
    extra_fare int NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS section
(
    id bigint auto_increment NOT NULL,
    line_id bigint NOT NULL,
    source_station_id bigint NOT NULL,
    target_station_id bigint NOT NULL,
    distance int NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(line_id) REFERENCES line(id) ON DELETE CASCADE,
    FOREIGN KEY(source_station_id) REFERENCES station(id) ON DELETE CASCADE,
    FOREIGN KEY(target_station_id) REFERENCES station(id) ON DELETE CASCADE
);
