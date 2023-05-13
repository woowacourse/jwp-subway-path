insert into station(id,name) values(1, '잠실');
insert into station(id,name) values(2, '잠실새내');
insert into station(id,name) values(3, '종합운동장');
insert into station(id,name) values(4, '삼성');
insert into station(id,name) values(5, '천호');


insert into line (id, name, color) values(1, '2호선', 'green');
insert into line (id, name, color) values(2, '8호선', 'red');
insert into line (id, name, color) values(3, '1호선', 'blue');

insert into section (id, up_bound_station_id, down_bound_station_id, line_id, distance) values (1, 1, 2, 1, 10);
insert into section (id, up_bound_station_id, down_bound_station_id, line_id, distance) values (2, 2, 3, 1, 10);

insert into section (id, up_bound_station_id, down_bound_station_id, line_id, distance) values (3, 5, 1, 2, 10);
