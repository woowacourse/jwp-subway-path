insert into line (id, name, color) values (1, '1번라인', '파랑색');

insert into station (id, name) values (1, 'st1');
insert into station (id, name) values (2, 'st2');
insert into station (id, name) values (3, 'st3');
insert into station (id, name) values (4, 'st4');
insert into station (id, name) values (5, 'st5');

insert into sections (id, up_id, down_id, line_id, distance, next_id) values (1, 1, 2, 1, 10, 2);
insert into sections (id, up_id, down_id, line_id, distance, next_id) values (2, 2, 3, 1, 10, null);
