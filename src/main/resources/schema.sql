create table if not exists STATION
(
    id        bigint auto_increment not null,
    domain_id varchar(36)           not null unique,
    name      varchar(255)          not null unique,
    primary key (id)
);

create table if not exists LINE
(
    id        bigint auto_increment not null,
    domain_id varchar(36)           not null unique,
    name      varchar(255)          not null unique,
    surcharge int                   not null,
    primary key (id)
);

create table if not exists SECTIONS
(
    id                     bigint auto_increment not null,
    up_station_domain_id   varchar(36)           not null,
    down_station_domain_id varchar(36)           not null,
    distance               int                   not null,
    line_domain_id         varchar(36)           not null,
    primary key (id),
    FOREIGN KEY (up_station_domain_id) REFERENCES STATION (domain_id),
    FOREIGN KEY (down_station_domain_id) REFERENCES STATION (domain_id),
    FOREIGN KEY (line_domain_id) REFERENCES LINE (domain_id)
);
