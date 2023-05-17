DELETE FROM line;
DELETE FROM station;
DELETE FROM section;

alter table line auto_increment = 1;
alter table station auto_increment = 1;
alter table section auto_increment = 1;
