INSERT INTO LINES (name, color)
VALUES ('1호선', '파랑');
INSERT INTO LINES (name, color)
VALUES ('2호선', '초록');

INSERT INTO STATIONS (name)
VALUES ('종합운동장');
INSERT INTO STATIONS (name)
VALUES ('잠실새내');
INSERT INTO STATIONS (name)
VALUES ('잠실');
INSERT INTO STATIONS (name)
VALUES ('강남');

INSERT INTO SECTIONS (line_id, up_station_id, down_station_id, distance)
VALUES (1, 1, 2, 5);

INSERT INTO SECTIONS (line_id, up_station_id, down_station_id, distance)
VALUES (1, 2, 3, 5);

INSERT INTO SECTIONS (line_id, up_station_id, down_station_id, distance)
VALUES (2, 4, 2, 5);
