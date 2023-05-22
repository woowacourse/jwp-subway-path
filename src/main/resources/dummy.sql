DROP TABLE SECTION IF EXISTS;
DROP TABLE LINE IF EXISTS;
DROP TABLE STATION IF EXISTS;

CREATE TABLE IF NOT EXISTS STATION
(
    id       BIGINT          AUTO_INCREMENT NOT NULL,
    name     VARCHAR(255)    NOT NULL UNIQUE,
    PRIMARY KEY(ID)
    );

CREATE TABLE IF NOT EXISTS LINE
(
    id       BIGINT          AUTO_INCREMENT NOT NULL,
    name     VARCHAR(255)    NOT NULL UNIQUE,
    color    VARCHAR(20)     NOT NULL,
    PRIMARY KEY(ID)
    );

CREATE TABLE IF NOT EXISTS SECTION
(
    id         BIGINT        AUTO_INCREMENT NOT NULL,
    line_id    BIGINT        NOT NULL,
    up_bound   BIGINT        NOT NULL,
    down_bound BIGINT        NOT NULL,
    distance   INT           NOT NULL,
    PRIMARY KEY(ID),
    FOREIGN KEY(up_bound)   REFERENCES STATION(id) ON DELETE RESTRICT,
    FOREIGN KEY(down_bound) REFERENCES STATION(id) ON DELETE RESTRICT
    );

INSERT INTO STATION (name) values ('잠실역');
INSERT INTO STATION (name) values ('방배역');
INSERT INTO STATION (name) values ('서초역');

INSERT INTO LINE (name, color) values ('1호선', '파랑');
INSERT INTO LINE (name, color) values ('2호선', '초록');
