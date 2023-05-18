INSERT INTO line (name, color)
VALUES ('2호선', '초록'),
       ('분당선', '노랑');

INSERT INTO station (name)
VALUES ('강남'),
       ('역삼'),
       ('선릉'),
       ('강남구청'),
       ('선정릉');

INSERT INTO section (first_station_id, second_station_id, distance, line_id)
VALUES (1, 2, 3, 1),
       (2, 3, 2, 1),
       (4, 5, 5, 2),
       (5, 3, 8, 2);

