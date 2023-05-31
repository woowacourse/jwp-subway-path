INSERT INTO LINE (name, surcharge) VALUES ('2호선', 1000);
INSERT INTO STATION (name, line_id) VALUES ('잠실역', 1);
INSERT INTO STATION (name, line_id) VALUES ('건대역', 1);
INSERT INTO SECTION (up_station_id, down_station_id, distance, line_id) VALUES (1, 2, 10, 1);