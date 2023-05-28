# 오즈의 편의를 위한 sql 입니다.
INSERT INTO line VALUES (1, '2호선');
INSERT INTO line VALUES (2, '4호선');
INSERT INTO line VALUES (3, '7호선');

# 2호선 역
INSERT INTO station VALUES(1, '낙성대역');
INSERT INTO station VALUES(2, '사당역');
INSERT INTO station VALUES(3, '방배역');
INSERT INTO station VALUES(4, '서초역');
INSERT INTO station VALUES(5, '교대역');

# 2호선 구간
INSERT INTO section(up_station_id, down_station_id, line_id, distance) VALUES (1, 2, 1, 2);
INSERT INTO section(up_station_id, down_station_id, line_id, distance) VALUES (2, 3, 1, 6);
INSERT INTO section(up_station_id, down_station_id, line_id, distance) VALUES (3, 4, 1, 4);
INSERT INTO section(up_station_id, down_station_id, line_id, distance) VALUES (4, 5, 1, 7);

# 4호선 역
INSERT INTO station VALUES(6, '동작역');
INSERT INTO station VALUES(7, '총신대입구역');
INSERT INTO station VALUES(8, '남태령역');

# 4호선 구간
INSERT INTO section(up_station_id, down_station_id, line_id, distance) VALUES (6, 2, 2, 20);
INSERT INTO section(up_station_id, down_station_id, line_id, distance) VALUES (2, 7, 2, 30);
INSERT INTO section(up_station_id, down_station_id, line_id, distance) VALUES (7, 8, 2, 35);

# 7호선 역
INSERT INTO station VALUES(9, '고속터미널역');
INSERT INTO station VALUES(10, '남부터미널역');

# 7호선 구간
INSERT INTO section(up_station_id, down_station_id, line_id, distance) VALUES (9, 5, 3, 5);
INSERT INTO section(up_station_id, down_station_id, line_id, distance) VALUES (5, 10, 3, 8);
