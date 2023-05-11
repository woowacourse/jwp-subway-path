insert into line(name, color) values ('2호선','초록색');
insert into line(name, color) values ('3호선','파란색');

insert into station(name) values ('신림역');
insert into station(name) values ('봉천역');
insert into station(name) values ('서울대입구역');
insert into station(name) values ('낙성대역');
insert into station(name) values ('사당역');
insert into station(name) values ('방배역');
insert into station(name) values ('서초역');
insert into station(name) values ('교대역');
insert into station(name) values ('강남역');
insert into station(name) values ('역삼역');
insert into station(name) values ('선릉역');

insert into section(line_id, up_station_id, down_station_id, distance) values (1L,1L,2L,1);
insert into section(line_id, up_station_id, down_station_id, distance) values (1L,2L,3L,1);
insert into section(line_id, up_station_id, down_station_id, distance) values (1L,3L,4L,1);
insert into section(line_id, up_station_id, down_station_id, distance) values (1L,4L,5L,1);
insert into section(line_id, up_station_id, down_station_id, distance) values (1L,5L,6L,1);
insert into section(line_id, up_station_id, down_station_id, distance) values (1L,6L,7L,1);

insert into section(line_id, up_station_id, down_station_id, distance) values (2L,8L,9L,2);
insert into section(line_id, up_station_id, down_station_id, distance) values (2L,9L,4L,2);
insert into section(line_id, up_station_id, down_station_id, distance) values (2L,4L,10L,2);
insert into section(line_id, up_station_id, down_station_id, distance) values (2L,10L,11L,2);
