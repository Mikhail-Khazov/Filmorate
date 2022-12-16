package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.RowMapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository("genreDbStorage")
@RequiredArgsConstructor
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public List<FilmGenre> getAll() {
        String sqlQuery = "SELECT * FROM genres";
        return jdbcTemplate.query(sqlQuery, RowMapper::mapRowToGenre);
    }

    public Optional<FilmGenre> getById(int id) {
        String sqlQuery = "SELECT * FROM genres WHERE GENRE_ID = ?";
        List<FilmGenre> genre = jdbcTemplate.query(sqlQuery, RowMapper::mapRowToGenre, id);
        return genre.isEmpty() ? Optional.empty() : Optional.of(genre.get(0));
    }

    public FilmGenre create(FilmGenre genre) {
        String sqlQuery = "INSERT INTO genres (TITLE) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((connection) -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"GENRE_ID"});
            statement.setString(1, genre.getName());
            return statement;
        }, keyHolder);
        genre.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return genre;
    }
}
