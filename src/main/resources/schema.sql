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
    upward_id bigint,
    downward_id bigint,
    distance int,
    line_id bigint not null,
    primary key(id),
    foreign key(upward_id) references STATION(id),
    foreign key(downward_id) references STATION(id),
    foreign key(line_id) references LINE(id)
);

INSERT INTO line(name, color) VALUES ('2호선', '초록색');
INSERT INTO station(name) VALUES ('잠실나루');
INSERT INTO station(name) VALUES ('잠실');
INSERT INTO station(name) VALUES ('강변');

