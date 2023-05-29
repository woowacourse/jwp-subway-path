DROP TABLE IF EXISTS SECTION;
DROP TABLE IF EXISTS LINE;
DROP TABLE IF EXISTS STATION;

create table if not exists STATION
(
    id      bigint          auto_increment not null,
    name    varchar(255)    not null unique,
    primary key(id)
);

create table if not exists LINE
(
    id          bigint          auto_increment not null,
    name        varchar(255)    not null unique,
    color       varchar(20)     not null,
    extra_fare  int             not null,
    primary key(id)
);

create table if not exists SECTION
(
    id          bigint              auto_increment not null,
    upward_id   bigint,
    downward_id bigint,
    distance    int,
    line_id     bigint              not null,
    primary     key(id),
    foreign     key(upward_id)      references STATION(id),
    foreign     key(downward_id)    references STATION(id),
    foreign     key(line_id)        references LINE(id)
);

INSERT INTO line(name, color, extra_fare) VALUES ('1호선', '남색', 0);
INSERT INTO line(name, color, extra_fare) VALUES ('2호선', '초록색', 0);
INSERT INTO line(name, color, extra_fare) VALUES ('8호선', '분홍색', 0);
INSERT INTO line(name, color, extra_fare) VALUES ('9호선', '황토색', 0);

INSERT INTO station(name) VALUES ('잠실나루');
INSERT INTO station(name) VALUES ('잠실');
INSERT INTO station(name) VALUES ('강변');
INSERT INTO station(name) VALUES ('노량진');
INSERT INTO station(name) VALUES ('용산');
INSERT INTO station(name) VALUES ('서울역');
INSERT INTO station(name) VALUES ('강동구청');
INSERT INTO station(name) VALUES ('몽촌토성');
INSERT INTO station(name) VALUES ('석촌');
INSERT INTO station(name) VALUES ('종합운동장');
INSERT INTO station(name) VALUES ('삼전');
INSERT INTO station(name) VALUES ('석촌고분');
INSERT INTO station(name) VALUES ('잠실새내');

INSERT INTO section(upward_id, downward_id, distance, line_id) VALUES (11, 10, 1, 4);
INSERT INTO section(upward_id, downward_id, distance, line_id) VALUES (12, 11, 1, 4);
INSERT INTO section(upward_id, downward_id, distance, line_id) VALUES (9, 12, 1, 4);

INSERT INTO section(upward_id, downward_id, distance, line_id) VALUES (3, 1, 3, 2);
INSERT INTO section(upward_id, downward_id, distance, line_id) VALUES (1, 2, 7, 2);
INSERT INTO section(upward_id, downward_id, distance, line_id) VALUES (2, 13, 1, 2);
INSERT INTO section(upward_id, downward_id, distance, line_id) VALUES (13, 10, 1, 2);

INSERT INTO section(upward_id, downward_id, distance, line_id) VALUES (7, 8, 15, 3);
INSERT INTO section(upward_id, downward_id, distance, line_id) VALUES (8, 2, 4, 3);
INSERT INTO section(upward_id, downward_id, distance, line_id) VALUES (2, 9, 9, 3);
