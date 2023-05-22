INSERT INTO STATION (name) VALUES ('A역');
INSERT INTO STATION (name) VALUES ('B역');
INSERT INTO STATION (name) VALUES ('C역');
INSERT INTO STATION (name) VALUES ('D역');
INSERT INTO STATION (name) VALUES ('E역');
INSERT INTO STATION (name) VALUES ('F역');
INSERT INTO STATION (name) VALUES ('G역');
INSERT INTO STATION (name) VALUES ('H역');
INSERT INTO STATION (name) VALUES ('I역');
INSERT INTO STATION (name) VALUES ('X역');
INSERT INTO STATION (name) VALUES ('Z역');
INSERT INTO STATION (name) VALUES ('?역');

INSERT INTO LINE (id, name, color, extra_fee) VALUES (1, '1호선', 'bg-blue-300', 0);
INSERT INTO LINE (id, name, color, extra_fee) VALUES (2, '2호선', 'bg-green-300', 100);
INSERT INTO LINE (id, name, color, extra_fee) VALUES (3, '3호선', 'bg-orange-300', 200);
INSERT INTO LINE (id, name, color, extra_fee) VALUES (4, '4호선', 'bg-skyblue-300', 300);

INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(1, 'A역', 'D역', 5);
INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(1, 'D역', 'E역', 8);
INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(1, 'E역', 'H역', 4);
INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(1, 'H역', 'I역', 2);

INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(2, 'A역', 'B역', 7);
INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(2, 'B역', 'C역', 8);
INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(2, 'C역', 'G역', 1);
INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(2, 'G역', 'I역', 1);

INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(3, 'D역', 'F역', 2);
INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(3, 'F역', 'G역', 3);

INSERT INTO SECTION(line_id, start_station_name, end_station_name, distance) VALUES(4, 'X역', 'Z역', 1);
