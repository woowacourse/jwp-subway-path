insert into Station (name)
values ('후추');
insert into Station (name)
values ('디노');
insert into Station (name)
values ('조앤');
insert into Station (name)
values ('로운');
insert into Station (name)
values ('포비');

insert into Line (name, color)
values ('2호선', 'Green');
insert into Line (name, color)
values ('8호선', 'pink');

insert into SECTION (line_id, from_id, to_id, distance)
values (1, 1, 2, 5);
insert into SECTION (line_id, from_id, to_id, distance)
values (1, 2, 3, 4);
insert into SECTION (line_id, from_id, to_id, distance)
values (1, 3, 4, 6);
insert into SECTION (line_id, from_id, to_id, distance)
values (2, 5, 4, 3);
insert into SECTION (line_id, from_id, to_id, distance)
values (2, 3, 5, 3);
