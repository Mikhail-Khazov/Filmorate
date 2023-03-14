package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage, GenreMapper {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<FilmGenre> getAll() {
        String sqlQuery = "SELECT * FROM genres";
        return jdbcTemplate.query(sqlQuery, GenreMapper::map);
    }

    @Override
    public Optional<FilmGenre> getById(Long id) {
        String sqlQuery = "SELECT * FROM genres WHERE GENRE_ID = ?";
        List<FilmGenre> genre = jdbcTemplate.query(sqlQuery, GenreMapper::map, id);
        return genre.isEmpty() ? Optional.empty() : Optional.of(genre.get(0));
    }

    @Override
    public FilmGenre create(FilmGenre genre) {
        String sqlQuery = "INSERT INTO genres (TITLE) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((connection) -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"GENRE_ID"});
            statement.setString(1, genre.getName());
            return statement;
        }, keyHolder);
        genre.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return genre;
    }

    @Override
    public void setGenres(List<Film> films) {
        final String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        final Map<Long, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, (f) -> f));
        if(inSql.isEmpty()) return;
        jdbcTemplate.query(
                String.format("SELECT fg.FILM_ID, fg.GENRE_ID, gn.TITLE " +
                        "FROM film_Genre AS fg " +
                        "LEFT JOIN genres AS gn ON fg.GENRE_ID = gn.GENRE_ID " +
                        "WHERE fg.FILM_ID IN (%s)", inSql),
                (rs) -> {
                    final Film film = filmById.get(rs.getLong("FILM_ID"));
                    film.addGenre(new FilmGenre(rs.getLong("GENRE_ID"), rs.getString("TITLE")));
                },
                films.stream().map(Film::getId).toArray());
    }
}
