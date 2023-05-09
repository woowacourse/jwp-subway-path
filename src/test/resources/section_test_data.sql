TRUNCATE TABLE section;
ALTER TABLE section auto_increment = 1;
INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
