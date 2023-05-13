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

create table if not exists LINE_STATION
(
    id bigint auto_increment not null,
    up_bound_id bigint not null,
    down_bound_id bigint not null,
    line_id bigint not null,
    distance int not null,
    primary key(id),
    foreign key(up_bound_id) references station(id) ON DELETE CASCADE,
    foreign key(down_bound_id) references station(id) ON DELETE CASCADE,
    foreign key(line_id) references line(id) ON DELETE CASCADE
);
