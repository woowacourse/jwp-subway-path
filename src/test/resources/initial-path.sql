insert into station(id, name) values (1, '잠실'), (2, '성수'), (3, '동대문역사문화공원'), (4, '혜화'), (10, '해운대');
insert into line(id, name, color) values (1, '2호선', '초록색'), (2, '4호선', '하늘색');
insert into station_edge(id, line_id, down_station_id, distance, previous_station_edge_id)
values (1, 1, 1, 0, null), (2, 1, 2, 4, 1), (3, 1, 3, 5, 2), (4, 2, 3, 0, null), (5, 2, 4, 9, 4);

