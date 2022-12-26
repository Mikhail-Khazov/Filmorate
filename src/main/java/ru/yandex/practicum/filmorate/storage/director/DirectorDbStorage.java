package ru.yandex.practicum.filmorate.storage.director;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage, DirectorMapper {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Director create(Director director) {
        final String sqlQuery = "INSERT INTO directors (DIRECTOR_NAME) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"DIRECTOR_ID"});
            ps.setString(1, director.getName());
            return ps;
        }, keyHolder);
        director.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return director;
    }

    @Override
    public int update(Director director) {
        final String sqlQuery = "UPDATE directors SET DIRECTOR_NAME = ? WHERE DIRECTOR_ID = ?";
        return jdbcTemplate.update(sqlQuery, director.getName(), director.getId());
    }

    @Override
    public Optional<Director> get(Long directorId) {
        final String sqlQuery = "SELECT DIRECTOR_ID, DIRECTOR_NAME FROM directors WHERE DIRECTOR_ID = ?";
        final List<Director> directors = jdbcTemplate.query(sqlQuery, DirectorMapper::map, directorId);

        if (directors.isEmpty()) return Optional.empty();
        return directors.stream().findFirst();
    }

    @Override
    public List<Director> getAll() {
        final String sqlQuery = "SELECT DIRECTOR_ID, DIRECTOR_NAME FROM directors";
        return jdbcTemplate.query(sqlQuery, DirectorMapper::map);
    }

    @Override
    public boolean delete(Long directorId) {
        final String sqlQuery = "DELETE FROM directors WHERE DIRECTOR_ID = ?";
        return jdbcTemplate.update(sqlQuery, directorId) > 0;
    }

    @Override
    public void setDirectors(List<Film> films) {
        final String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        final Map<Long, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, (f) -> f));
        if(inSql.isEmpty()) return;

        jdbcTemplate.query(
                String.format("SELECT fd.FILM_ID, fd.DIRECTOR_ID, d.DIRECTOR_NAME " +
                        "FROM film_directors AS fd " +
                        "LEFT JOIN directors AS d ON fd.DIRECTOR_ID = d.DIRECTOR_ID " +
                        "WHERE fd.FILM_ID IN (%s)", inSql),
                (rs) -> {
                    final Film film = filmById.get(rs.getLong("FILM_ID"));

                    if (null != film.getDirectors()) {
                        film.addDirector(new Director(rs.getLong("DIRECTOR_ID"), rs.getString("DIRECTOR_NAME")));
                    }
                },
                films.stream().map(Film::getId).toArray());
    }
}
