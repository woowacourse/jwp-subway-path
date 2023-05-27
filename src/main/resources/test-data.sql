INSERT INTO station(stationId, name)
VALUES (1, '잠실역'),
       (2, '잠실새내역'),
       (3, '선릉역'),
       (4, '강남역'),
       (5, '교대역'),

       (6, '신논현역'),
       (7, '양재역'),

       (8, '신사역'),
       (9, '고속터미널역'),
       (10, '남부터미널역');

INSERT INTO line(lineId, name)
VALUES (1, '2호선'),
       (2, '신분당선'),
       (3, '3호선'),
       (4, '분당선');

INSERT INTO section(sectionId, lineId, upStationId, downStationId, distance)
VALUES (1, 1, 1, 2, 3),
       (2, 1, 2, 3, 2),
       (3, 1, 3, 4, 1),
       (4, 1, 4, 5, 3),
       (5, 2, 6, 5, 3),
       (6, 2, 5, 7, 1),
       (7, 2, 7, 8, 1),
       (8, 3, 3, 9, 1),
       (9, 4, 3, 10, 1),
       (10, 3, 9, 7, 1);
