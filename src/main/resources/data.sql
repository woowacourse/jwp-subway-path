insert ignore into LINE (id, name, color) VALUES(1, '1호선', 'blue');
insert ignore into LINE (id, name, color) VALUES(2, '2호선', 'green');
insert ignore into LINE (id, name, color) VALUES(3, '3호선', 'orange');

insert ignore into STATION (id, name) VALUES(1, '시청역');
insert ignore into STATION (id, name) VALUES(2, '을지로3가역');
insert ignore into STATION (id, name) VALUES(3, '을지로4가역');
insert ignore into STATION (id, name) VALUES(4, '동대문역사문화공원역');
insert ignore into STATION (id, name) VALUES(5, '신당역');
insert ignore into STATION (id, name) VALUES(6, '상왕십리역');
insert ignore into STATION (id, name) VALUES(7, '서울역');
insert ignore into STATION (id, name) VALUES(8, '종각역');
insert ignore into STATION (id, name) VALUES(9, '종로3가역');
insert ignore into STATION (id, name) VALUES(10, '종로5가역');

insert ignore into SECTIONS (id, line_id, left_station_id, right_station_id, distance) VALUES(1, 1, 2, 3, 10);
insert ignore into SECTIONS (id, line_id, left_station_id, right_station_id, distance) VALUES(2, 1, 3, 4, 10);
insert ignore into SECTIONS (id, line_id, left_station_id, right_station_id, distance) VALUES(3, 1, 4, 5, 10);
insert ignore into SECTIONS (id, line_id, left_station_id, right_station_id, distance) VALUES(4, 1, 1, 2, 8);
insert ignore into SECTIONS (id, line_id, left_station_id, right_station_id, distance) VALUES(5, 1, 5, 6, 10);

insert ignore into SECTIONS (id, line_id, left_station_id, right_station_id, distance) VALUES(6, 2, 7, 1, 10);
insert ignore into SECTIONS (id, line_id, left_station_id, right_station_id, distance) VALUES(7, 2, 1, 8, 10);
insert ignore into SECTIONS (id, line_id, left_station_id, right_station_id, distance) VALUES(8, 2, 8, 9, 10);
insert ignore into SECTIONS (id, line_id, left_station_id, right_station_id, distance) VALUES(9, 2, 9, 10, 10);

insert ignore into SECTIONS (id, line_id, left_station_id, right_station_id, distance) VALUES(10, 3, 2, 9, 10);

