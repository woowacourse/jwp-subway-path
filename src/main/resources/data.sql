INSERT INTO station(name) VALUES('온수'), ('천왕'), ('광명사거리'), ('철산'), ('가산디지털단지'), ('남구로'), ('대림'),
                                ('오류동'), ('개봉'), ('구일'), ('구로'), ('신도림'), ('구로디지털단지'), ('신대방');
INSERT INTO line(name, color) VALUES('1호선', 'bg-blue-600'), ('2호선', 'bg-green-600'), ('7호선', 'bg-olive-600');
INSERT INTO section(line_id, distance, previous_station_id, next_station_id) VALUES(3, 4, 1, 2),
                                                                                   (3, 4, 2, 3),
                                                                                   (3, 4, 3, 4),
                                                                                   (3, 4, 4, 5),
                                                                                   (3, 4, 5, 6),
                                                                                   (3, 4, 6, 7),
                                                                                   (1, 4, 1, 8),
                                                                                   (1, 4, 8, 9),
                                                                                   (1, 4, 9, 10),
                                                                                   (1, 4, 10, 11),
                                                                                   (1, 4, 11, 5),
                                                                                   (2, 4, 12, 7),
                                                                                   (2, 4, 7, 13),
                                                                                   (2, 4, 13, 14);
