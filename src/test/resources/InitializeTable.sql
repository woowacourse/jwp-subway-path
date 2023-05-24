SET REFERENTIAL_INTEGRITY FALSE;
truncate table SECTION restart identity ;
truncate table LINE restart identity ;
truncate table STATION restart identity ;


insert into line (name, color) values ('1호선', '남색');
insert into line (name, color, additional_fee) values ('2호선', '초록색', 200);
insert into station (name) values ('신도림');
insert into station (name) values ('영등포구청');
insert into station (name) values ('신대방');
insert into station (name) values ('신림');
insert into section (source_station_id, target_station_id, line_id, distance) values (1L, 2L, 2L, 10);
insert into section (source_station_id, target_station_id, line_id, distance) values (2L, 3L, 2L, 10);
SET REFERENTIAL_INTEGRITY TRUE;

