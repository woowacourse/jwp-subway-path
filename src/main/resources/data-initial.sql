INSERT INTO line (name, color)
VALUES ('2호선', '초록');

INSERT INTO station (name)
VALUES ('강남'),
       ('역삼'),
       ('선릉');

INSERT INTO section (first_station_id, second_station_id, distance, line_id)
VALUES (1, 2, 3, 1),
       (2, 3, 2, 1);

