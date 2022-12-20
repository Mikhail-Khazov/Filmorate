package ru.yandex.practicum.filmorate.storage.director;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.storage.RowMapper;
import ru.yandex.practicum.filmorate.model.Director;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Director create(Director director) {
        final String sqlQuery = "INSERT INTO director (DIRECTOR_NAME) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"DIRECTOR_ID"});
            ps.setString(1, director.getName());
            return ps;
        }, keyHolder);
        director.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return director;
    }

    @Override
    public int update(Director director) {
        final String sqlQuery = "UPDATE director SET DIRECTOR_NAME = ? WHERE DIRECTOR_ID = ?";
        return jdbcTemplate.update(sqlQuery, director.getName(), director.getId());
    }

    @Override
    public Optional<Director> get(int directorId) {
        final String sqlQuery = "SELECT DIRECTOR_ID, DIRECTOR_NAME FROM director WHERE DIRECTOR_ID = ?";
        final List<Director> directors = jdbcTemplate.query(sqlQuery, RowMapper::mapRowToDirector, directorId);

        if (directors.isEmpty()) return Optional.empty();
        Director director = directors.get(0);
        return Optional.of(director);
    }

    @Override
    public List<Director> getAll() {
        final String sqlQuery = "SELECT DIRECTOR_ID, DIRECTOR_NAME FROM director";
        return jdbcTemplate.query(sqlQuery, RowMapper::mapRowToDirector);
    }

    @Override
    public Set<Director> getDirectorByFilmId(int id) {
        return new HashSet<>(jdbcTemplate.query(String.format("SELECT d.DIRECTOR_ID, d.DIRECTOR_NAME " +
                "FROM director AS d " +
                "JOIN directors AS ds ON d.DIRECTOR_ID = ds.DIRECTOR_ID " +
                "WHERE ds.FILM_ID = %d " +
                "ORDER BY d.DIRECTOR_ID", id), RowMapper::mapRowToDirectorSorting));
    }

    @Override
    public boolean delete(int directorId) {
        final String sqlQuery = "DELETE FROM director WHERE DIRECTOR_ID = ?";
        return jdbcTemplate.update(sqlQuery, directorId) > 0;
    }
}
