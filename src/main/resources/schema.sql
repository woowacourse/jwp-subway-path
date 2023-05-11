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
    primary key(id)
);

create table if not exists SECTION
(
    id bigint auto_increment not null,
    up_station varchar(255) not null,
    down_station varchar(255) not null,
    distance int not null,
    line_id int not null,
    foreign key (line_id) references line (id) on delete cascade,
    foreign key (up_station) references station (name) on delete cascade,
    foreign key (down_station) references station (name) on delete cascade,
    primary key (id)
);
