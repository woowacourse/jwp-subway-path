INSERT INTO LINE (name) VALUES ('2호선');
INSERT INTO LINE (name) VALUES ('7호선');
INSERT INTO LINE (name) VALUES ('3호선');

INSERT INTO STATION (name, line_id) VALUES ('A역', 1);
INSERT INTO STATION (name, line_id) VALUES ('C역', 1);
INSERT INTO STATION (name, line_id) VALUES ('A역', 2);
INSERT INTO STATION (name, line_id) VALUES ('D역', 2);
INSERT INTO STATION (name, line_id) VALUES ('C역', 3);
INSERT INTO STATION (name, line_id) VALUES ('D역', 3);
INSERT INTO STATION (name, line_id) VALUES ('E역', 3);

INSERT INTO GENERAL_SECTION (up_station_id, down_station_id, line_id, distance) VALUES (1, 2, 1, 10);
INSERT INTO GENERAL_SECTION (up_station_id, down_station_id, line_id, distance) VALUES (5, 7, 3, 8);
INSERT INTO GENERAL_SECTION (up_station_id, down_station_id, line_id, distance) VALUES (3, 4, 2, 12);
INSERT INTO GENERAL_SECTION (up_station_id, down_station_id, line_id, distance) VALUES (6, 7, 3, 5);
