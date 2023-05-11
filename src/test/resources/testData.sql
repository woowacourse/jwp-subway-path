insert into station(id,name) values(1, '잠실');
insert into station(id,name) values(2, '잠실새내');
insert into line (id, name, color) values(1, '2호선', 'green');
insert into line_station (id, up_bound_id, down_bound_id, line_id, distance) values (1, 1, 2, 1, 10);
