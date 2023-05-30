INSERT INTO station (id, name) VALUES (1, '강남');
INSERT INTO station (id, name) VALUES (2, '잠실');
INSERT INTO station (id, name) VALUES (3, '몽촌토성');
INSERT INTO station (id, name) VALUES (4, '암사');
INSERT INTO station (id, name) VALUES (5, '길동');

INSERT INTO line (id, name, color) VALUES (1, '1호선', 'blue');

INSERT INTO sections (up_id, down_id, distance, line_id) VALUES (1, 2, 5, 1);
INSERT INTO sections (up_id, down_id, distance, line_id) VALUES (2, 3, 5, 1);
INSERT INTO sections (up_id, down_id, distance, line_id) VALUES (3, 4, 5, 1);
INSERT INTO sections (up_id, down_id, distance, line_id) VALUES (4, 5, 5, 1);
