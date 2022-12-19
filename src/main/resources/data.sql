MERGE INTO mpa (RATING_ID, MPA)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');

MERGE INTO genres (GENRE_ID, TITLE)
    VALUES (1,'Комедия'),
           (2,'Драма'),
           (3,'Мультфильм'),
           (4,'Триллер'),
           (5,'Документальный'),
           (6,'Боевик');

-- MERGE INTO films (FILM_ID, FILM_NAME, RELEASE_DATE, DESCRIPTION, DURATION, RATING_ID)
--     VALUES (1, 'New film', '1999-04-30', 'New film about friends', 120, 3),
--            (2, 'Home alone', '1991-12-30', 'Lorem iosum dolor sit amet', 530, 2)