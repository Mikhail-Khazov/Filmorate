package ru.yandex.practicum.filmorate.storage.mpaaRating;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPAAFilmRating;
import ru.yandex.practicum.filmorate.storage.RowMapper;

import java.util.List;
import java.util.Optional;

@Repository("mpaaRatingDbStorage")
@RequiredArgsConstructor
public class MPAARatingDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public List<MPAAFilmRating> getAll() {
        String sqlQuery = "SELECT * FROM mpa";
        return jdbcTemplate.query(sqlQuery, RowMapper::mapRowToMPAAFilmRating);
    }

    public Optional<MPAAFilmRating> getById(int id) {
        String sqlQuery = "SELECT * FROM mpa WHERE RATING_ID = ?";
        List<MPAAFilmRating> mpa = jdbcTemplate.query(sqlQuery, RowMapper::mapRowToMPAAFilmRating, id);
        return mpa.isEmpty() ? Optional.empty() : Optional.of(mpa.get(0));
    }
}
