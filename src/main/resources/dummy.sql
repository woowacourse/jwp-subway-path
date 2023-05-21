
INSERT INTO line (id, name, color, extra_fare) VALUES (1, '2호선', '초록색', 300);
INSERT INTO station (id, name) VALUES (1, '잠실역');
INSERT INTO station (id, name) VALUES (2, '잠실새내역');
INSERT INTO section (id, line_id, upward_station_id, downward_station_id, distance) VALUES (1, 1, 1, 2, 10);
