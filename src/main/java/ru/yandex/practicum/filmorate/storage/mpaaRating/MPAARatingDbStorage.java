package ru.yandex.practicum.filmorate.storage.mpaaRating;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPAAFilmRating;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MPAARatingDbStorage implements MPAARatingStorage, MPAAFilmRatingMapper {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<MPAAFilmRating> getAll() {
        String sqlQuery = "SELECT * FROM mpa";
        return jdbcTemplate.query(sqlQuery, MPAAFilmRatingMapper::map);
    }

    @Override
    public Optional<MPAAFilmRating> getById(Long id) {
        String sqlQuery = "SELECT * FROM mpa WHERE RATING_ID = ?";
        List<MPAAFilmRating> mpa = jdbcTemplate.query(sqlQuery, MPAAFilmRatingMapper::map, id);
        return mpa.isEmpty() ? Optional.empty() : Optional.of(mpa.get(0));
    }
}
