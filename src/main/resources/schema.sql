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


create table if not exists SECTIONS
(
    id bigint auto_increment not null,
    line_id bigint not null,
    left_station_id bigint not null,
    right_station_id bigint not null,
    distance int not null,
    primary key (id),
    constraint line_fk foreign key (line_id) references LINE (id),
    constraint left_station_fk foreign key (left_station_id) references STATION (id),
    constraint right_station_fk foreign key (right_station_id) references STATION (id)
    );
