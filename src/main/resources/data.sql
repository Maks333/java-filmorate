--ratings
MERGE INTO ratings r
    USING (VALUES ('G') , ('PG'), ('PG-13'), ('R'), ('NC-17')) AS s (source)
    ON r.name = s.source
WHEN MATCHED THEN
    UPDATE
        SET r.name = source
WHEN NOT MATCHED THEN
    INSERT(name) VALUES(source);

--genres
MERGE INTO genres g
    USING (VALUES ('Комедия') , ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик')) AS s (source)
    ON g.name = s.source
WHEN MATCHED THEN
    UPDATE
        SET g.name = source
WHEN NOT MATCHED THEN
    INSERT(name) VALUES(source);