CREATE TABLE IF NOT EXISTS mpa
(
    RATING_ID INT NOT NULL PRIMARY KEY auto_increment,
    MPA       VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS films
(
    FILM_ID      INT          NOT NULL PRIMARY KEY auto_increment,
    FILM_NAME    VARCHAR(100) NOT NULL,
    DESCRIPTION  VARCHAR(200) NOT NULL,
    RELEASE_DATE DATE         NOT NULL CHECK (RELEASE_DATE > '1895-12-28'),
    DURATION     LONG         NOT NULL CHECK (DURATION > 0),
    RATING_ID    INT,
    FOREIGN KEY (RATING_ID) REFERENCES mpa (RATING_ID)
);

CREATE TABLE IF NOT EXISTS users
(
    USER_ID   INT         NOT NULL PRIMARY KEY auto_increment,
    EMAIL     VARCHAR(50) NOT NULL UNIQUE,
    LOGIN     VARCHAR(50) NOT NULL UNIQUE,
    USER_NAME VARCHAR(50),
    BIRTHDAY  DATE        NOT NULL
);

CREATE TABLE IF NOT EXISTS directors
(
    DIRECTOR_ID   INT NOT NULL PRIMARY KEY auto_increment,
    DIRECTOR_NAME VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_directors
(
    FILM_ID     INT NOT NULL,
    DIRECTOR_ID INT NOT NULL,
    FOREIGN KEY (FILM_ID) REFERENCES films (FILM_ID) ON DELETE CASCADE,
    FOREIGN KEY (DIRECTOR_ID) REFERENCES directors (DIRECTOR_ID) ON DELETE CASCADE,
    PRIMARY KEY (FILM_ID, DIRECTOR_ID)
);

CREATE TABLE IF NOT EXISTS genres
(
    GENRE_ID INT NOT NULL PRIMARY KEY auto_increment,
    TITLE    VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_Genre
(
    FILM_ID  INT NOT NULL,
    GENRE_ID INT NOT NULL,
    FOREIGN KEY (FILM_ID) REFERENCES films (FILM_ID) ON DELETE CASCADE,
    FOREIGN KEY (GENRE_ID) REFERENCES genres (GENRE_ID) ON DELETE CASCADE,
    PRIMARY KEY (FILM_ID, GENRE_ID)
);

CREATE TABLE IF NOT EXISTS friends
(
    USER_ID   INT NOT NULL,
    FRIEND_ID INT NOT NULL,
    FOREIGN KEY (USER_ID) REFERENCES users (USER_ID) ON DELETE CASCADE,
    FOREIGN KEY (FRIEND_ID) REFERENCES users (USER_ID) ON DELETE CASCADE,
    PRIMARY KEY (USER_ID, FRIEND_ID)
);

CREATE TABLE IF NOT EXISTS liked
(
    FILM_ID INT NOT NULL,
    USER_ID INT NOT NULL,
    FOREIGN KEY (FILM_ID) REFERENCES films (FILM_ID) ON DELETE CASCADE,
    FOREIGN KEY (USER_ID) REFERENCES users (USER_ID) ON DELETE CASCADE,
    PRIMARY KEY (FILM_ID, USER_ID)
);

CREATE VIEW IF NOT EXISTS FILMS_VIEW_SEARCH AS (
    SELECT f.FILM_ID AS id, f.FILM_NAME AS f_name, d.DIRECTOR_NAME AS d_name
    FROM films AS f
    LEFT JOIN film_directors AS fd ON f.FILM_ID = fd.FILM_ID
    LEFT JOIN directors AS d ON d.DIRECTOR_ID = fd.DIRECTOR_ID
);