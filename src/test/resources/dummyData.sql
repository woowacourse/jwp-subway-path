INSERT INTO station(name) VALUES ('강변');
INSERT INTO station(name) VALUES ('잠실나루');
INSERT INTO station(name) VALUES ('잠실');
INSERT INTO station(name) VALUES ('잠실새내');
INSERT INTO station(name) VALUES ('종합운동장');

INSERT INTO station(name) VALUES ('서울역');
INSERT INTO station(name) VALUES ('용산');
INSERT INTO station(name) VALUES ('노량진');
INSERT INTO station(name) VALUES ('노들');
INSERT INTO station(name) VALUES ('흑석');

INSERT INTO station(name) VALUES ('동작');
INSERT INTO station(name) VALUES ('삼각지');
INSERT INTO station(name) VALUES ('이촌');

INSERT INTO line(name, color, extra_fare) VALUES ('1호선', '남색', 400);
INSERT INTO section(upward_id, downward_id, distance, line_id) VALUES (null, 6, null, 1);
INSERT INTO section(upward_id, downward_id, distance, line_id) VALUES (6,7, 12, 1);
INSERT INTO section(upward_id, downward_id, distance, line_id) VALUES (7,8, 20, 1);
INSERT INTO section(upward_id, downward_id, distance, line_id) VALUES (8, null, null, 1);
