INSERT INTO station(station_id, name)
VALUES (1, '잠실역'),
       (2, '잠실새내역'),
       (3, '종합운동장역'),
       (4, '삼성역'),
       (5, '선릉역'),

       (6, '팔당역'),
       (7, '매봉역');

INSERT INTO line(line_id, line_number, name, color)
VALUES (1, 2, '2호선', '초록색'),
       (2, 8, '8호선', '핑크색');

-- 2호선) 잠실 --3--> 잠실새내 --2--> 종합운동장 --1--> 삼성 --3--> *선릉(8호선과 겹침)*
-- 8호선  팔당 --3--> *선릉(2호선과 겹침)* --2--> 매봉

INSERT INTO section(section_id, line_id, up_station_id, down_station_id, distance)
VALUES (1, 1, 1, 2, 3),
       (2, 1, 2, 3, 2),
       (3, 1, 3, 4, 1),
       (4, 1, 4, 5, 3),
       (5, 2, 6, 5, 3),
       (6, 2, 5, 7, 2),
       (7, 2, 1, 7, 10);
