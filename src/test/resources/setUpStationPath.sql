delete from sections;
delete from station;
delete from line;

insert into line (id, name, color) values (1, '1번라인', '파랑색');
insert into line (id, name, color) values (2, '2번라인', '검정색');

insert into station (id, name) values (1, 'st1');
insert into station (id, name) values (2, 'st2');
insert into station (id, name) values (3, 'st3');
insert into station (id, name) values (4, 'st4');
insert into station (id, name) values (5, 'st5');
insert into station (id, name) values (6, 'st6');
insert into station (id, name) values (7, 'st7');
insert into station (id, name) values (8, 'st8');
insert into station (id, name) values (9, 'st9');

insert into sections (uuid, up_id, down_id, line_id, distance) values (UUID(), 1, 2, 1, 1);
insert into sections (uuid, up_id, down_id, line_id, distance) values (UUID(), 2, 3, 1, 2);
insert into sections (uuid, up_id, down_id, line_id, distance) values (UUID(), 3, 7, 1, 3);

insert into sections (uuid, up_id, down_id, line_id, distance) values (UUID(), 2, 4, 2, 5);
insert into sections (uuid, up_id, down_id, line_id, distance) values (UUID(), 4, 5, 2, 5);
insert into sections (uuid, up_id, down_id, line_id, distance) values (UUID(), 5, 6, 2, 5);
insert into sections (uuid, up_id, down_id, line_id, distance) values (UUID(), 6, 8, 2, 5);
insert into sections (uuid, up_id, down_id, line_id, distance) values (UUID(), 8, 9, 2, 5);
insert into sections (uuid, up_id, down_id, line_id, distance) values (UUID(), 9, 7, 2, 10);

-- 1 - 2 - 3 - 7
--     |       |
--     4-5-6-8-9