INSERT INTO LINE (name) VALUES ('2호선');
INSERT INTO LINE (name) VALUES ('7호선');
INSERT INTO STATION (name, line_id) VALUES ('A역', 1);
INSERT INTO STATION (name, line_id) VALUES ('C역', 1);
INSERT INTO STATION (name, line_id) VALUES ('A역', 2);
INSERT INTO GENERAL_SECTION (up_station_id, down_station_id, line_id, distance) VALUES (1, 2, 1, 10);
