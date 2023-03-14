package ru.yandex.practicum.filmorate.storage.mpaaRating;

import ru.yandex.practicum.filmorate.model.MPAAFilmRating;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface MPAAFilmRatingMapper {
    static MPAAFilmRating map(ResultSet rs, long row) throws SQLException {
        return new MPAAFilmRating(rs.getLong("RATING_ID"),
                rs.getString("MPA")
        );
    }
}
