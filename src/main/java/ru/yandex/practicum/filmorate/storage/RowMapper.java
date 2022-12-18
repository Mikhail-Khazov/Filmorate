package ru.yandex.practicum.filmorate.storage;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.MPAAFilmRating;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class RowMapper {
    public static User mapRowToUser(ResultSet rs, int row) throws SQLException {
        return new User(rs.getInt("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("BIRTHDAY").toLocalDate()
        );
    }

    public static Film mapRowToFilm(ResultSet rs, int row) throws SQLException {
        return new Film(rs.getInt("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getLong("DURATION"),
                new MPAAFilmRating(rs.getInt("RATING_ID"), rs.getString("MPA"))
        );
    }

    public static FilmGenre mapRowToGenre(ResultSet rs, int row) throws SQLException {
        return new FilmGenre(rs.getInt("GENRE_ID"),
                rs.getString("TITLE")
        );
    }

    public static MPAAFilmRating mapRowToMPAAFilmRating(ResultSet rs, int row) throws SQLException {
        return new MPAAFilmRating(rs.getInt("RATING_ID"),
                rs.getString("MPA")
        );
    }
}
