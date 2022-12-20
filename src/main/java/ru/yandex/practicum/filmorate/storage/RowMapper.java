package ru.yandex.practicum.filmorate.storage;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.*;

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

    public static Director mapRowToDirector(ResultSet rs, int row) throws SQLException {
        return new Director(rs.getInt("DIRECTOR_ID"),
                rs.getString("DIRECTOR_NAME")
        );
    }

    public static Director mapRowToDirectorSorting(ResultSet rs, int row) throws SQLException {
        Director director = new Director();
        director.setId(rs.getInt("DIRECTOR_ID"));
        director.setName(rs.getString("DIRECTOR_NAME"));
        return director;
    }

    public static Film mapRowToFilmSorting(ResultSet rs, int row) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("FILM_ID"));
        film.setName(rs.getString("FILM_NAME"));
        film.setDuration(rs.getInt("DURATION"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        MPAAFilmRating mpa = new MPAAFilmRating(rs.getInt("RATING_ID"), rs.getString("MPA"));
        film.setMpa(mpa);
        return film;
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
