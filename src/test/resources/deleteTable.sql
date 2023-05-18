DELETE FROM line;
ALTER TABLE line ALTER COLUMN id restart;
DELETE FROM section;
ALTER TABLE section ALTER COLUMN id restart;