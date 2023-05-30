DELETE FROM station;
DELETE FROM line;

ALTER TABLE station ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE line ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE section ALTER COLUMN ID RESTART WITH 1;

INSERT INTO station(name)
VALUES ('1역'),
       ('2역'),
       ('3역'),
       ('4역'),
       ('5역'),
       ('6역');

INSERT INTO line(name, color)
VALUES ('1호선', 'bg-red-500'),
       ('2호선', 'bg-blue-500');
