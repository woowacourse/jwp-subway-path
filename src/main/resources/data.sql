CREATE TABLE IF NOT EXISTS station
(
    `id` BIGINT AUTO_INCREMENT NOT NULL,
    `name` VARCHAR(15) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS line
(
    `id` BIGINT AUTO_INCREMENT NOT NULL,
    `name` VARCHAR(15) NOT NULL,
    `color` VARCHAR(15) NOT NULL,
    `fare` INT NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS section
(
    `id` BIGINT AUTO_INCREMENT NOT NULL,
    `line_id` BIGINT NOT NULL,
    `upward_station_id` BIGINT NOT NULL,
    `downward_station_id` BIGINT NOT NULL,
    `distance` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`line_id`) REFERENCES `line` (`id`),
    FOREIGN KEY (`upward_station_id`) REFERENCES `station` (`id`),
    FOREIGN KEY (`downward_station_id`) REFERENCES `station` (`id`)
);
