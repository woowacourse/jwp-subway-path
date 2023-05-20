INSERT INTO line
VALUES (1, '1호선', '파랑'),
       (2, '2호선', '초록'),
       (3, 'empty', 'none');

INSERT INTO station
VALUES (1, '수원'),
       (2, '잠실나루'),
       (3, '의왕'),
       (4, '선릉');

INSERT INTO paths
VALUES (1, 1, 1, 2, 5),
       (2, 2, 3, 1, 5),
       (3, 2, 1, 4, 7);
