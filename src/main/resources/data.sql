INSERT INTO STATION (id, name) VALUES (1, '교대역');
INSERT INTO STATION (id, name) VALUES (2, '강남역');
INSERT INTO STATION (id, name) VALUES (3, '역삼역');
INSERT INTO STATION (id, name) VALUES (4, '남부터미널역');

INSERT INTO LINE (id, name, color) VALUES (1, '2호선', 'GREEN');
INSERT INTO LINE (id, name, color) VALUES (2, '3호선', 'ORANGE');
INSERT INTO LINE (id, name, color) VALUES (3, '신분당선', 'RED');

INSERT INTO INTERSTATION (id, line_id, distance, up_station_id, down_station_id) VALUES (1, 1, 2, 1, 2);
INSERT INTO INTERSTATION (id, line_id, distance, up_station_id, down_station_id) VALUES (2, 1, 2, 2, 3);
INSERT INTO INTERSTATION (id, line_id, distance, up_station_id, down_station_id) VALUES (3, 2, 3, 3, 4);
INSERT INTO INTERSTATION (id, line_id, distance, up_station_id, down_station_id) VALUES (4, 3, 2, 1, 3);
