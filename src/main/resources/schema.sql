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
    color varchar(20) not null unique,
    extra_fare int not null,
    primary key(id)
);

create table if not exists SECTION
(
    id bigint auto_increment not null,
    upward_id bigint,
    downward_id bigint,
    distance int,
    line_id bigint not null,
    primary key(id),
    foreign key(upward_id) references STATION(id),
    foreign key(downward_id) references STATION(id),
    foreign key(line_id) references LINE(id)
);

