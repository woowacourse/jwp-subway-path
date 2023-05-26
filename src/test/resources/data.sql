insert into station (name)
values ('잠실나루'),
       ('잠실'),
       ('잠실새내'),
       ('정자'),
       ('판교'),
       ('종합운동장'),
       ('몽촌토성'),
       ('석촌'),
       ('A'),
       ('B'),
       ('C'),
       ('D'),
       ('E'),
       ('F');

insert into line (name, color)
values ('1호선', 'blue'),
       ('2호선', 'green'),
       ('3호선', 'brown'),
       ('99호선', 'gray'),
       ('100호선', 'white');

insert into section (line_id, station_id, next_station_id, distance)
values (4, 9, 11, 1),  -- 99호선, A->C
       (4, 11, 12, 1), -- 99호선, C->D
       (4, 12, 13, 6), -- 99호선, D->E
       (5, 9, 10, 1),  -- 100호선, A->B
       (5, 10, 11, 1), -- 100호선, B->C
       (5, 11, 13, 6); -- 100호선, C->E

