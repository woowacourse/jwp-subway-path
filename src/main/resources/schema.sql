CREATE SCHEMA IF NOT EXISTS subway;

DROP TABLE IF EXISTS section;
DROP TABLE IF EXISTS station;
DROP TABLE IF EXISTS line;

CREATE TABLE IF NOT EXISTS station
(
    station_id BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255)          NOT NULL UNIQUE,
    primary key (station_id)
);

CREATE TABLE IF NOT EXISTS line
(
    line_id     BIGINT AUTO_INCREMENT NOT NULL,
    line_number BIGINT(255)           NOT NULL UNIQUE,
    name        VARCHAR(255)          NOT NULL UNIQUE,
    color       VARCHAR(20)           NOT NULL,
    PRIMARY KEY (line_id)
);

CREATE TABLE IF NOT EXISTS section
(
    section_id      BIGINT AUTO_INCREMENT NOT NULL,
    line_id         BIGINT                NOT NULL,
    up_station_id   BIGINT                NOT NULL,
    down_station_id BIGINT                NOT NULL,
    distance        BIGINT                NOT NULL,
    PRIMARY KEY (section_id),
    FOREIGN KEY (line_id) REFERENCES line (line_id),
    FOREIGN KEY (up_station_id) REFERENCES station (station_id),
    FOREIGN KEY (down_station_id) REFERENCES station (station_id)
);

INSERT INTO station(station_id, name)
VALUES (1, '잠실역'),
       (2, '잠실새내역'),
       (3, '종합운동장역'),
       (4, '삼성역'),
       (5, '선릉역'),

       (6, '팔당역'),
       (7, '매봉역'),

--       step2 추가
       (8, '판교역'),
       (9, '정자역'),
       (10, '논현역');

INSERT INTO line(line_id, line_number, name, color)
VALUES (1, 2, '2호선', '초록색'),
       (2, 8, '8호선', '핑크색'),
       (3, 9, '9호선', '빨간색'),
       (4, 10, '10호선', '노란색');

-- 2호선) 잠실 --3--> 잠실새내 --2--> 종합운동장 --1--> 삼성 --3--> *선릉(8호선과 겹침)*
-- 8호선                                          팔당 --3--> *선릉(2호선과 겹침)* --2--> 매봉 --1--> 판교

INSERT INTO section(section_id, line_id, up_station_id, down_station_id, distance)
VALUES (1, 1, 1, 2, 3),
       (2, 1, 2, 3, 2),
       (3, 1, 3, 4, 1),
       (4, 1, 4, 5, 3),
       (5, 2, 6, 5, 3),
       (6, 2, 5, 7, 1),
       (7, 2, 7, 8, 1),  -- 매봉 <-> 판교
       (8, 3, 3, 9, 1),  -- 종운 <-> 정자
       (9, 4, 3, 10, 1), -- 종운 <-> 논현역
       (10, 3, 9, 7, 1); -- 정자 <-> 매봉
