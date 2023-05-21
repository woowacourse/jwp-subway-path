-- 노선
INSERT INTO line(id, name) VALUES (1, "2호선");
INSERT INTO line(id, name) VALUES (2, "3호선");
INSERT INTO line(id, name) VALUES (3, "4호선");
INSERT INTO line(id, name) VALUES (4, "7호선");

-- 역
INSERT INTO station(id, line_id, name) VALUES(1, 1, "봉천");
INSERT INTO station(id, line_id, name) VALUES(2, 1, "서울대입구");
INSERT INTO station(id, line_id, name) VALUES(3, 1, "낙성대");
INSERT INTO station(id, line_id, name) VALUES(4, 1, "사당");
INSERT INTO station(id, line_id, name) VALUES(5, 1, "방배");
INSERT INTO station(id, line_id, name) VALUES(6, 1, "서초");
INSERT INTO station(id, line_id, name) VALUES(7, 1, "교대");
INSERT INTO station(id, line_id, name) VALUES(8, 1, "강남");

INSERT INTO station(id, line_id, name) VALUES(9, 2, "남부터미널");
INSERT INTO station(id, line_id, name) VALUES(10, 2, "교대");
INSERT INTO station(id, line_id, name) VALUES(11, 2, "고속터미널");
INSERT INTO station(id, line_id, name) VALUES(12, 2, "잠원");

INSERT INTO station(id, line_id, name) VALUES(13,3, "남태령");
INSERT INTO station(id, line_id, name) VALUES(14,3, "사당");
INSERT INTO station(id, line_id, name) VALUES(15,3, "총신대입구");
INSERT INTO station(id, line_id, name) VALUES(16,3, "동작");

INSERT INTO station(id, line_id, name) VALUES(17, 4, "남성");
INSERT INTO station(id, line_id, name) VALUES(18, 4, "이수");
INSERT INTO station(id, line_id, name) VALUES(19, 4, "내방");
INSERT INTO station(id, line_id, name) VALUES(20, 4, "고속터미널");
INSERT INTO station(id, line_id, name) VALUES(21, 4, "반포");

-- 구간
INSERT INTO section(id, line_id, upward_station_id, downward_station_id, distance) VALUES (1, 1, 1, 2, 5);
INSERT INTO section(id, line_id, upward_station_id, downward_station_id, distance) VALUES (2, 1, 2, 3, 5);
INSERT INTO section(id, line_id, upward_station_id, downward_station_id, distance) VALUES (3, 1, 3, 4, 5);
INSERT INTO section(id, line_id, upward_station_id, downward_station_id, distance) VALUES (4, 1, 4, 5, 5);
INSERT INTO section(id, line_id, upward_station_id, downward_station_id, distance) VALUES (5, 1, 5, 6, 5);
INSERT INTO section(id, line_id, upward_station_id, downward_station_id, distance) VALUES (6, 1, 6, 7, 5);
INSERT INTO section(id, line_id, upward_station_id, downward_station_id, distance) VALUES (7, 1, 7, 8, 5);

INSERT INTO section(id, line_id, upward_station_id, downward_station_id, distance) VALUES (8, 2, 9, 10, 5);
INSERT INTO section(id, line_id, upward_station_id, downward_station_id, distance) VALUES (9, 2, 10, 11, 5);
INSERT INTO section(id, line_id, upward_station_id, downward_station_id, distance) VALUES (10, 2, 11, 12, 5);

INSERT INTO section(id, line_id, upward_station_id, downward_station_id, distance) VALUES (11, 3, 13, 14, 5);
INSERT INTO section(id, line_id, upward_station_id, downward_station_id, distance) VALUES (12, 3, 14, 15, 5);
INSERT INTO section(id, line_id, upward_station_id, downward_station_id, distance) VALUES (13, 3, 15, 16, 5);

INSERT INTO section(id, line_id, upward_station_id, downward_station_id, distance) VALUES (14, 4, 17, 18, 5);
INSERT INTO section(id, line_id, upward_station_id, downward_station_id, distance) VALUES (15, 4, 18, 19, 5);
INSERT INTO section(id, line_id, upward_station_id, downward_station_id, distance) VALUES (16, 4, 19, 20, 5);
INSERT INTO section(id, line_id, upward_station_id, downward_station_id, distance) VALUES (17, 4, 20, 21, 5);

