INSERT INTO LINE (name, color, head_station)
values('1호선', '파란색', 1);

INSERT INTO STATION (name, next_station, distance, line_id)
values('강남역', 2, 10, 1);

INSERT INTO STATION (name, next_station, distance, line_id)
values('역삼역', 4, 5, 1);

INSERT INTO STATION (name, next_station, distance, line_id)
values('잠실역', 0, null, 1);

INSERT INTO STATION (name, next_station, distance, line_id)
values('선릉역', 6, 10, 1);

INSERT INTO STATION (name, next_station, distance, line_id)
values('건대입구역', 3, 10, 1);

INSERT INTO STATION (name, next_station, distance, line_id)
values('삼성역', 5, 8, 1);