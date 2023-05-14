DELETE FROM line;
DELETE FROM station;
DELETE FROM section;

ALTER TABLE line auto_increment = 1;
ALTER TABLE station auto_increment = 1;
ALTER TABLE section auto_increment = 1;
