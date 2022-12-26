package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DirectorMapper {
    static Director map(ResultSet rs, long row) throws SQLException {
        return new Director(rs.getLong("DIRECTOR_ID"),
                rs.getString("DIRECTOR_NAME")
        );
    }
}
