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
    additional_fare int not null,
    primary key(id)
);

create table if not exists SECTION
(
    id bigint auto_increment not null,
    upstream_id bigint not null,
    downstream_id bigint not null,
    line_id bigint not null,
    distance int not null,
    primary key(id),
    foreign key(upstream_id) references STATION(id) ON DELETE CASCADE,
    foreign key(downstream_id) references STATION(id) ON DELETE CASCADE,
    foreign key(line_id) references LINE(id) ON DELETE CASCADE
);
