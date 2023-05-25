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
    upper_station bigint not null,
    lower_station bigint not null,
    distance int not null,
    primary key (id),
    foreign key (line_id) references LINE (id) on delete cascade,
    foreign key (upper_station) references STATION (id) on delete cascade,
    foreign key (lower_station) references STATION (id) on delete cascade,
    constraint not_same_station check (upper_station != lower_station),
    constraint only_positive_distance check (distance > 0)
);
