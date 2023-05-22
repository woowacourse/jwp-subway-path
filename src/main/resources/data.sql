INSERT INTO station(name) VALUES('가산'), ('남구로'), ('대림'), ('신풍'), ('구로'), ('독산'), ('신도림'), ('구로디지털단지');
INSERT INTO line(name, color) VALUES('7호선', 'bg-olive-600'), ('1호선', 'bg-blue-600'), ('2호선', 'bg-green-600');
INSERT INTO section(line_id, distance, previous_station_id, next_station_id) VALUES(1, 10, 1, 2),
                                                                                   (1, 10, 2, 3),
                                                                                   (1, 10, 3, 4),
                                                                                   (2, 10, 5, 2),
                                                                                   (2, 10, 2, 6),
                                                                                   (3, 10, 7, 3),
                                                                                   (3, 10, 3, 8);
INSERT INTO fare_policy(line_id, additional_fare) VALUES(1, 0),
                                                        (2, 500),
                                                        (3, 900);