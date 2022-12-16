package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
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
import java.util.stream.Collectors;

@Repository("filmDbStorage")
@RequiredArgsConstructor
@Primary
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
    public Film update(Film film) {
        String sqlQuery = "UPDATE films SET FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_ID = ? WHERE FILM_ID = ?";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        writeGenreToDB(film);
        return get(film.getId()).orElseThrow();
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
        setGenreForPOJO(film);
//        setLikesForPOJO(film);
        return Optional.of(film);
    }

    private void setGenreForPOJO(Film film) {
        String sqlQuery = "SELECT g.GENRE_ID, g.TITLE " +
                "FROM filmGenre AS fg " +
                "LEFT JOIN genres AS g ON fg.GENRE_ID = g.GENRE_ID " +
                "WHERE fg.FILM_ID = ?";
        Set<FilmGenre> genreSet = new HashSet<>(jdbcTemplate.query(sqlQuery, RowMapper::mapRowToGenre, film.getId()));
        film.setGenres(genreSet);
    }

    @Override
    public List<Film> getAll() {
        final String sqlQuery = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, f.RATING_ID, r.MPA " +
                "FROM films AS f " +
                "LEFT JOIN mpa AS r ON f.RATING_ID = r.RATING_ID ";
        final List<Film> films = jdbcTemplate.query(sqlQuery, RowMapper::mapRowToFilm);
        films.forEach(this::setGenreForPOJO);
        return films;
    }

    private void writeGenreToDB(Film film) {
        String sqlQueryDelete = "DELETE FROM filmGenre WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQueryDelete, film.getId());
        if (null == film.getGenres() || film.getGenres().isEmpty()) {
            return;
        }
        List<FilmGenre> genres = film.getGenres().stream().distinct().collect(Collectors.toList());
        StringBuilder sqlQuery = new StringBuilder();
        for (FilmGenre filmGenre : genres) {
            String query = String.format("INSERT INTO filmGenre (FILM_ID, GENRE_ID) VALUES (%d, %d); ", film.getId(), filmGenre.getId());
            sqlQuery.append(query);
        }
        jdbcTemplate.update(sqlQuery.toString());
    }

    public MPAAFilmRating getMpaaRating(int filmId) {
        Film film = get(filmId).orElseThrow();
        return film.getMpa();
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
        films.forEach(this::setGenreForPOJO);
        return films;
    }
}
