INSERT INTO line (name, color)
VALUES ('2호선', '초록');

INSERT INTO station (name)
VALUES ('강남'),
       ('역삼'),
       ('선릉');

INSERT INTO section (first_station, second_station, distance, line_id)
VALUES ('강남', '역삼', 3, 1),
       ('역삼', '선릉', 2, 1);

