package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.MPAAFilmRating;
import ru.yandex.practicum.filmorate.storage.RowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO films (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((connection) -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, Date.valueOf(film.getReleaseDate()));
            statement.setLong(4, film.getDuration());
            final MPAAFilmRating ratingId = film.getMpa();
            if (null == ratingId) statement.setNull(5, Types.INTEGER);
            else statement.setInt(5, ratingId.getId());
            return statement;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        writeGenreToDB(film);
        return film;
    }

    @Override
    public int update(Film film) {
        String sqlQuery = "UPDATE films " +
                "SET FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_ID = ? " +
                "WHERE FILM_ID = ?";

        int updatedRowCount = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        deleteAllGenres(film.getId());
        writeGenreToDB(film);
        return updatedRowCount;
    }

    @Override
    public Optional<Film> get(int filmId) {
        final String sqlQuery = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, f.RATING_ID, r.MPA " +
                "FROM films AS f " +
                "LEFT JOIN mpa AS r ON f.RATING_ID = r.RATING_ID " +
                "WHERE FILM_ID = ? ";
        final List<Film> films = jdbcTemplate.query(sqlQuery, RowMapper::mapRowToFilm, filmId);
        if (films.isEmpty()) {
            return Optional.empty();
        }
        Film film = films.get(0);
        return Optional.of(film);
    }

    @Override
    public List<Film> getAll() {
        final String sqlQuery = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, f.RATING_ID, r.MPA " +
                "FROM films AS f " +
                "LEFT JOIN mpa AS r ON f.RATING_ID = r.RATING_ID ";
        final List<Film> films = jdbcTemplate.query(sqlQuery, RowMapper::mapRowToFilm);
        return films;
    }

    private void deleteAllGenres(int id) {
        String sqlQueryDelete = "DELETE FROM film_Genre WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQueryDelete, id);
    }

    private void writeGenreToDB(Film film) {
        if (null == film.getGenres() || film.getGenres().isEmpty()) {
            return;
        }
        List<FilmGenre> genres = new ArrayList<>(film.getGenres());
        jdbcTemplate.batchUpdate(
                "INSERT INTO film_Genre (FILM_ID, GENRE_ID) VALUES (?, ?); ",
                genres,
                genres.size(),
                (PreparedStatement ps, FilmGenre genre) -> {
                    ps.setInt(1, film.getId());
                    ps.setInt(2, genre.getId());
                }
        );
    }

    public MPAAFilmRating getMpaaRating(int filmId) {
        Film film = get(filmId).orElseThrow();
        return film.getMpa();
    }

    @Override
    public List<Film> getCommonFilms(int userId, int friendId) {
        final String sqlQuery = "SELECT f. *, r. * " +
                                "FROM films AS f " +
                                "JOIN mpa AS r ON f.RATING_ID = r.RATING_ID " +
                                "JOIN liked AS l ON f.FILM_ID = l.FILM_ID WHERE L.USER_ID = ? " +
                                "INTERSECT SELECT f. *, r. * " +
                                "FROM films AS f " +
                                "JOIN mpa AS r ON f.RATING_ID = r.RATING_ID " +
                                "JOIN liked AS l ON f.FILM_ID = l.FILM_ID WHERE L.USER_ID = ? ;" ;

        final List<Film> films = jdbcTemplate.query(sqlQuery, RowMapper::mapRowToFilm,userId,friendId);
        return films;
    }

    public List<Film> getTopFilms(int count) {
        String sqlQuery = "SELECT f. *, r.MPA " +
                "FROM films AS f " +
                "LEFT JOIN liked AS l ON f.FILM_ID = l.FILM_ID " +
                "LEFT JOIN  mpa AS r ON f.RATING_ID = r.RATING_ID " +
                "GROUP BY f.FILM_ID " +
                "ORDER BY COUNT (l.USER_ID) DESC " +
                "LIMIT ? ";
        final List<Film> films = jdbcTemplate.query(sqlQuery, RowMapper::mapRowToFilm, count);
        return films;
    }

    @Override
    public boolean delete(int filmId) {
        final String sqlQuery = "DELETE FROM films WHERE FILM_ID = ?";
        return jdbcTemplate.update(sqlQuery, filmId) > 0;
    }
}
