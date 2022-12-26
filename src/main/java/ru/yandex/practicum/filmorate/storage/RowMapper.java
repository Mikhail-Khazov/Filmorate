package ru.yandex.practicum.filmorate.storage;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.feed.EventType;
import ru.yandex.practicum.filmorate.storage.feed.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class RowMapper {
    public static User mapRowToUser(ResultSet rs, int row) throws SQLException {
        return new User(rs.getLong("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("BIRTHDAY").toLocalDate()
        );
    }

    public static Film mapRowToFilm(ResultSet rs, int row) throws SQLException {
        return new Film(rs.getLong("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getLong("DURATION"),
                new MPAAFilmRating(rs.getLong("RATING_ID"), rs.getString("MPA"))
        );
    }

    public static Director mapRowToDirector(ResultSet rs, int row) throws SQLException {
        return new Director(rs.getLong("DIRECTOR_ID"),
                rs.getString("DIRECTOR_NAME")
        );
    }

    public static FilmGenre mapRowToGenre(ResultSet rs, int row) throws SQLException {
        return new FilmGenre(rs.getLong("GENRE_ID"),
                rs.getString("TITLE")
        );
    }

    public static MPAAFilmRating mapRowToMPAAFilmRating(ResultSet rs, int row) throws SQLException {
        return new MPAAFilmRating(rs.getLong("RATING_ID"),
                rs.getString("MPA")
        );
    }

    public static Review mapRowToReview(ResultSet rs, int row) throws SQLException {
        return new Review(rs.getLong("REVIEW_ID"),
                rs.getString("CONTENT"),
                rs.getBoolean("IS_POSITIVE"),
                rs.getLong("USER_ID"),
                rs.getLong("FILM_ID"),
                rs.getInt("USEFULNESS")
        );
    }

    public static FeedRow mapRowToFeedRow(ResultSet rs, int row) throws SQLException {
        return new FeedRow(rs.getLong("EVENT_ID"),
                rs.getLong("USER_ID"),
                rs.getLong("ENTITY_ID"),
                EventType.toEnum(rs.getString("EVENT_TYPE")),
                Operation.toEnum(rs.getString("OPERATION")),
                rs.getLong("TIMELONG")
        );
    }
}
