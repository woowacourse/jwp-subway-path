insert into STATION (name) values ('서울대입구역');
insert into STATION (name) values ('사당역');
insert into STATION (name) values ('선릉역');
insert into STATION (name) values ('잠실역');
insert into STATION (name) values ('잠실새내역');

insert into LINE (name, color) values ('1호선', '파란색');
insert into LINE (name, color) values ('2호선', '초록색');

insert into SECTION (line_id, up_station_id, down_station_id, distance) values (1, 1, 2, 3);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (1, 2, 3, 5);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (2, 2, 4, 1);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (2, 4, 3, 1);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (2, 3, 5, 10);
