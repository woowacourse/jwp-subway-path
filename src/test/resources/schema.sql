ALTER TABLE SECTIONS DROP CONSTRAINT line_fk;
ALTER TABLE SECTIONS DROP CONSTRAINT left_station_fk;
ALTER TABLE SECTIONS DROP CONSTRAINT right_station_fk;
TRUNCATE TABLE LINE RESTART IDENTITY;
TRUNCATE TABLE STATION RESTART IDENTITY;
TRUNCATE TABLE SECTIONS RESTART IDENTITY;
ALTER TABLE SECTIONS ADD constraint line_fk foreign key (line_id) references LINE (id);
ALTER TABLE SECTIONS ADD constraint left_station_fk foreign key (left_station_id) references STATION (id);
ALTER TABLE SECTIONS ADD constraint right_station_fk foreign key (right_station_id) references STATION (id);
