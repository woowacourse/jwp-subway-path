INSERT INTO LINE (name) VALUES ('2호선');
INSERT INTO STATION (name, line_id) VALUES ('잠실역', 1);
INSERT INTO STATION (name, line_id) VALUES ('건대역', 1);
INSERT INTO SUBWAY_SECTION (up_station_id, down_station_id, line_id, distance) VALUES (1, 2, 1, 10);