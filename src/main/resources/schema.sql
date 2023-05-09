create table if not exists STATION
(
    name varchar(255) not null,

    primary key (name)
);

create table if not exists LINE_NODE
(
    id              BIGINT        not null AUTO_INCREMENT,
    before_node     BIGINT,
    station         varchar(255)  not null,
    next_node       BIGINT,
    before_distance INT default 0 not null,
    next_distance   INT default 0 not null,
    primary key (id),
    foreign key (before_node) references LINE_NODE (id),
    foreign key (next_node) references LINE_NODE (id),
    foreign key (station) references STATION (name)
);

create table if not exists LINE
(
    id   bigint auto_increment not null,
    name varchar(255)          not null unique,
    primary key (id)
);
