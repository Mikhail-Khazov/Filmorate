package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPAAFilmRating;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface FilmMapper {
    static Film map(ResultSet rs, long row) throws SQLException {
        return new Film(rs.getLong("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getLong("DURATION"),
                new MPAAFilmRating(rs.getLong("RATING_ID"), rs.getString("MPA"))
        );
    }
}
