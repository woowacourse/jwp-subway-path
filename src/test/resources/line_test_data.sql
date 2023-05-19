TRUNCATE TABLE line;
ALTER TABLE line auto_increment = 1;
INSERT INTO line(name, color)
VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
