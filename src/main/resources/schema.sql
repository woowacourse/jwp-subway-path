DROP TABLE if exists SECTIONS;
DROP TABLE if exists STATION;
DROP TABLE if exists LINE;

create table if not exists STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,
    primary key(id)
);

create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    color varchar(20) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,
    primary key(id)
);

create table if not exists SECTIONS
(
    id bigint auto_increment not null,
    uuid varchar(36) not null,
    up_id bigint not null,
    down_id bigint not null,
    line_id bigint not null,
    distance int not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,
    primary key(id),
    FOREIGN KEY (up_id) REFERENCES STATION(id) ON DELETE CASCADE,
    FOREIGN KEY (down_id) REFERENCES STATION(id) ON DELETE CASCADE,
    FOREIGN KEY (line_id) REFERENCES LINE(id) ON DELETE CASCADE
);
