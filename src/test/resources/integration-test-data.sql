INSERT INTO LINE (name) VALUES ('2호선');
INSERT INTO LINE (name) VALUES ('8호선');
INSERT INTO LINE (name) VALUES ('7호선');

INSERT INTO STATION (name, line_id) VALUES ('선릉역', 1);
INSERT INTO STATION (name, line_id) VALUES ('잠실역', 1);
INSERT INTO STATION (name, line_id) VALUES ('건대역', 1);
INSERT INTO SECTION (up_station_id, down_station_id, distance, line_id) VALUES (1, 2, 5, 1);
INSERT INTO SECTION (up_station_id, down_station_id, distance, line_id) VALUES (2, 3, 10, 1);

INSERT INTO STATION (name, line_id) VALUES ('잠실역', 2);
INSERT INTO STATION (name, line_id) VALUES ('암사역', 2);
INSERT INTO SECTION (up_station_id, down_station_id, distance, line_id) VALUES (4, 5, 8, 2);
