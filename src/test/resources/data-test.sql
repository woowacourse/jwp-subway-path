INSERT INTO LINE (name, color, extra_charge, head_station)
values('1호선', '파란색',0, 1);

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



INSERT INTO LINE (name, color,extra_charge, head_station)
values('2호선', '초록색',300, 7);

INSERT INTO STATION (name, next_station, distance, line_id)
values('성수역', 8, 10, 2);

INSERT INTO STATION (name, next_station, distance, line_id)
values('뚝섬역', 9, 5, 2);

INSERT INTO STATION (name, next_station, distance, line_id)
values('잠실역', 10, 15, 2);

INSERT INTO STATION (name, next_station, distance, line_id)
values('삼성역', 11, 10, 2);

INSERT INTO STATION (name, next_station, distance, line_id)
values('왕십리역', 12, 4, 2);

INSERT INTO STATION (name, next_station, distance, line_id)
values('용답역', 0, null, 2);

INSERT INTO LINE (name, color,extra_charge, head_station)
values('3호선', '주황색',700, 13);

INSERT INTO STATION (name, next_station, distance, line_id)
values('노포역', 14, 40, 3);

INSERT INTO STATION (name, next_station, distance, line_id)
values('서면역', 0, null, 3);