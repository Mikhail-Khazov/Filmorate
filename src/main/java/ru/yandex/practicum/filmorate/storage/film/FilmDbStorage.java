package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.model.MPAAFilmRating;
import ru.yandex.practicum.filmorate.storage.RowMapper;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final DirectorDbStorage directorDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO films (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) VALUES (?, ?, ?, ?, ?)";
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
        writeDirectorToDB(film);
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

        deleteAllDirectors(film.getId());
        writeDirectorToDB(film);
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
        return jdbcTemplate.query(sqlQuery, RowMapper::mapRowToFilm);
    }

    private void deleteAllGenres(int id) {
        final String sqlQueryDelete = "DELETE FROM film_Genre WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQueryDelete, id);
    }

    private void writeGenreToDB(Film film) {
        if (null == film.getGenres() || film.getGenres().isEmpty()) return;
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

    private void deleteAllDirectors(int id) {
        final String sqlQueryDelete = "DELETE FROM directors WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQueryDelete, id);
    }

    private void writeDirectorToDB(Film film) {
        if (null == film.getDirectors() || film.getDirectors().isEmpty()) return;
        List<Director> directors = new ArrayList<>(film.getDirectors());
        jdbcTemplate.batchUpdate(
                "INSERT INTO directors (FILM_ID, DIRECTOR_ID) VALUES (?, ?); ",
                directors,
                directors.size(),
                (PreparedStatement ps, Director director) -> {
                    ps.setInt(1, film.getId());
                    ps.setInt(2, director.getId());
                }
        );
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
        return jdbcTemplate.query(sqlQuery, RowMapper::mapRowToFilm, count);
    }

    @Override
    public boolean delete(int filmId) {
        final String sqlQuery = "DELETE FROM films WHERE FILM_ID = ?";
        return jdbcTemplate.update(sqlQuery, filmId) > 0;
    }

    @Override
    public List<Film> getSortedFilms(int directorId, String sortBy) {
        if ("year".equals(sortBy)) {
            final String sqlQuery = "SELECT f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, m.RATING_ID, m.MPA " +
                    "FROM films AS f " +
                    "JOIN mpa AS m ON m.RATING_ID = f.RATING_ID " +
                    "JOIN directors AS d ON f.FILM_ID = d.film_id " +
                    "WHERE d.DIRECTOR_ID = ?" +
                    "ORDER BY f.RELEASE_DATE";
            return getFilms(directorId, sqlQuery);
        } else if ("likes".equals(sortBy)) {
            String sqlQuery = "SELECT f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, " +
                    "f.DURATION, m.RATING_ID, m.MPA, COUNT(l.USER_ID)" +
                    "FROM films AS f " +
                    "Left JOIN liked AS l ON f.FILM_ID = l.FILM_ID " +
                    "Left JOIN MPA AS m ON m.RATING_ID = f.RATING_ID " +
                    "Left JOIN directors AS d ON f.FILM_ID = d.FILM_ID " +
                    "WHERE d.DIRECTOR_ID = ? " +
                    "GROUP BY f.FILM_ID " +
                    "ORDER BY COUNT(l.USER_ID) DESC ";
            return getFilms(directorId, sqlQuery);
        } else {
            throw new RuntimeException("Извините, данный вариант сортировки отсутствует");
        }
    }

    private List<Film> getFilms(int directorId, String sqlQuery) {
        List<Film> films = jdbcTemplate.query(sqlQuery, RowMapper::mapRowToFilmSorting, directorId);
        for (Film film : films) {
            film.getGenres().addAll(genreDbStorage.getFilmGenres(film.getId()));
            film.getLikes().addAll(getUserLikes(film.getId()));
            film.getDirectors().addAll(directorDbStorage.getDirectorByFilmId(film.getId()));
        }
        return films;
    }

    private List<Integer> getUserLikes(int filmId) {
        final String select = "SELECT USER_ID " +
                "FROM liked " +
                "WHERE FILM_ID = ?";
        return jdbcTemplate.query(select, (rs, rowNum) -> rs.getInt("USER_ID"), filmId);
    }
}
