delete from sections;
delete from station;
delete from line;

insert into line (id, name, color) values (1, '1번라인', '파랑색');

insert into station (id, name) values (1, 'st1');
insert into station (id, name) values (2, 'st2');
insert into station (id, name) values (3, 'st3');
insert into station (id, name) values (4, 'st4');
insert into station (id, name) values (5, 'st5');

insert into sections (uuid, up_id, down_id, line_id, distance) values (UUID(), 1, 2, 1, 10);
insert into sections (uuid, up_id, down_id, line_id, distance) values (UUID(), 2, 3, 1, 10);
