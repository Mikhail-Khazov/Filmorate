package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface GenreMapper {
    static FilmGenre map(ResultSet rs, long row) throws SQLException {
        return new FilmGenre(rs.getLong("GENRE_ID"),
                rs.getString("TITLE")
        );
    }
}
