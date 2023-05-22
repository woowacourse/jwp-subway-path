create table if not exists STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
);

create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    color varchar(20) not null,
    primary key(id)
);

create table if not exists SECTION
(
    id bigint auto_increment not null,
    line_id bigint not null,
    up_station_id bigint not null,
    down_station_id bigint not null,
    distance bigint not null,
    primary key(id),
    foreign key(line_id) references LINE(id),
    foreign key(up_station_id) references STATION(id),
    foreign key(down_station_id) references STATION(id)
);

insert into STATION (name) values ('왕십리역');
insert into STATION (name) values ('한양대역');
insert into STATION (name) values ('뚝섬역');
insert into STATION (name) values ('성수역');
insert into STATION (name) values ('건대입구역');
insert into STATION (name) values ('구의역');
insert into STATION (name) values ('강변역');
insert into STATION (name) values ('잠실나루역');
insert into STATION (name) values ('잠실역');
insert into STATION (name) values ('잠실새내역');
insert into STATION (name) values ('종합운동장역');
insert into STATION (name) values ('삼성역');
insert into STATION (name) values ('선릉역');
insert into STATION (name) values ('역삼역');
insert into STATION (name) values ('강남역');

insert into LINE (name, color) values ('2호선', 'GREEN');

insert into SECTION (line_id, up_station_id, down_station_id, distance) values (1, 1, 2, 5);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (1, 2, 3, 5);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (1, 3, 4, 5);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (1, 4, 5, 5);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (1, 5, 6, 5);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (1, 6, 7, 5);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (1, 7, 8, 5);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (1, 8, 9, 5);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (1, 9, 10, 5);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (1, 10, 11, 5);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (1, 11, 12, 5);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (1, 12, 13, 5);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (1, 13, 14, 5);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (1, 14, 15, 5);

insert into STATION (name) values ('서울숲역');
insert into STATION (name) values ('압구정로데오역');
insert into STATION (name) values ('강남구청역');
insert into STATION (name) values ('선정릉역');

insert into LINE (name, color) values ('수인분당선', 'YELLOW');

insert into SECTION (line_id, up_station_id, down_station_id, distance) values (2, 1, 16, 2);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (2, 16, 17, 2);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (2, 17, 18, 2);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (2, 18, 19, 2);

insert into STATION (name) values ('마장역');
insert into STATION (name) values ('답십리역');
insert into STATION (name) values ('장한평역');
insert into STATION (name) values ('군자역');
insert into STATION (name) values ('아차산역');
insert into STATION (name) values ('광나루역');
insert into STATION (name) values ('천호역');

insert into LINE (name, color) values ('5호선', 'PURPLE');

insert into SECTION (line_id, up_station_id, down_station_id, distance) values (3, 1, 20, 6);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (3, 20, 21, 6);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (3, 21, 22, 6);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (3, 22, 23, 6);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (3, 23, 24, 6);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (3, 24, 25, 6);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (3, 25, 26, 6);

insert into STATION (name) values ('청담역');
insert into STATION (name) values ('뚝섬유원지역');
insert into STATION (name) values ('어린이대공원역');

insert into LINE (name, color) values ('7호선', 'DARKGREEN');

insert into SECTION (line_id, up_station_id, down_station_id, distance) values (4, 17, 27, 10);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (4, 27, 28, 10);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (4, 28, 5, 10);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (4, 5, 29, 10);

insert into STATION (name) values ('몽촌토성역');
insert into STATION (name) values ('강동구청역');

insert into LINE (name, color) values ('8호선', 'PINK');

insert into SECTION (line_id, up_station_id, down_station_id, distance) values (5, 9, 30, 3);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (5, 30, 31, 3);
insert into SECTION (line_id, up_station_id, down_station_id, distance) values (5, 31, 26, 3);