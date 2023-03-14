package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.MPAAFilmRating;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage, FilmMapper {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        final String sqlQuery = "INSERT INTO films (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((connection) -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, Date.valueOf(film.getReleaseDate()));
            statement.setLong(4, film.getDuration());
            final MPAAFilmRating ratingId = film.getMpa();
            if (null == ratingId) statement.setNull(5, Types.INTEGER);
            else statement.setLong(5, ratingId.getId());
            return statement;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        writeGenreToDB(film);
        writeDirectorToDB(film);
        return film;
    }

    @Override
    public int update(Film film) {
        final String sqlQuery = "UPDATE films " +
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
        deleteDirectors(film.getId());
        writeDirectorToDB(film);
        return updatedRowCount;
    }

    @Override
    public Optional<Film> get(Long filmId) {
        final String sqlQuery = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, f.RATING_ID, r.MPA " +
                "FROM films AS f " +
                "LEFT JOIN mpa AS r ON f.RATING_ID = r.RATING_ID " +
                "WHERE FILM_ID = ? ";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmMapper::map, filmId);
        return films.stream().findFirst();
    }

    @Override
    public List<Film> getAll() {
        final String sqlQuery = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, f.RATING_ID, r.MPA " +
                "FROM films AS f " +
                "LEFT JOIN mpa AS r ON f.RATING_ID = r.RATING_ID ";
        return jdbcTemplate.query(sqlQuery, FilmMapper::map);
    }

    private void deleteAllGenres(Long id) {
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
                    ps.setLong(1, film.getId());
                    ps.setLong(2, genre.getId());
                }
        );
    }

    private void deleteDirectors(Long id) {
        final String sqlQueryDelete = "DELETE FROM film_directors WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQueryDelete, id);
    }

    private void writeDirectorToDB(Film film) {
        if (null == film.getDirectors() || film.getDirectors().isEmpty()) return;
        List<Director> directors = new ArrayList<>(film.getDirectors());
        jdbcTemplate.batchUpdate(
                "INSERT INTO film_directors (FILM_ID, DIRECTOR_ID) VALUES (?, ?); ",
                directors,
                directors.size(),
                (PreparedStatement ps, Director director) -> {
                    ps.setLong(1, film.getId());
                    ps.setLong(2, director.getId());
                }
        );
    }

    public MPAAFilmRating getMpaaRating(Long filmId) {
        Film film = get(filmId).orElseThrow();
        return film.getMpa();
    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) {
        final String sqlQuery = "SELECT f. *, r. * " +
                "FROM films AS f " +
                "JOIN mpa AS r ON f.RATING_ID = r.RATING_ID " +
                "JOIN liked AS l ON f.FILM_ID = l.FILM_ID WHERE L.USER_ID = ? " +
                "INTERSECT SELECT f. *, r. * " +
                "FROM films AS f " +
                "JOIN mpa AS r ON f.RATING_ID = r.RATING_ID " +
                "JOIN liked AS l ON f.FILM_ID = l.FILM_ID WHERE L.USER_ID = ? ;";

        return jdbcTemplate.query(sqlQuery, FilmMapper::map, userId, friendId);
    }

    public List<Film> getTopFilms(int count, Integer genreId, Integer year) {
        String sqlQueryIfGenreId = "AND fg.GENRE_ID = ? ";
        if (genreId == null) sqlQueryIfGenreId = "";
        final String sqlQuery = "SELECT f.*, r.MPA " +
                "FROM films AS f " +
                "LEFT JOIN liked AS l ON f.FILM_ID = l.FILM_ID " +
                "LEFT JOIN mpa AS r ON f.RATING_ID = r.RATING_ID " +
                "LEFT JOIN film_genre AS fg ON f.FILM_ID = fg.FILM_ID " +
                "WHERE EXTRACT(year FROM f.RELEASE_DATE) LIKE ifnull(?, '%') " +
                sqlQueryIfGenreId +
                "GROUP BY f.FILM_ID " +
                "ORDER BY COUNT(l.USER_ID) DESC " +
                "LIMIT ? ";
        if (genreId == null) return jdbcTemplate.query(sqlQuery, FilmMapper::map, year, count);
        return jdbcTemplate.query(sqlQuery, FilmMapper::map, year, genreId, count);
    }

    @Override
    public boolean delete(Long filmId) {
        final String sqlQuery = "DELETE FROM films WHERE FILM_ID = ?";
        return jdbcTemplate.update(sqlQuery, filmId) > 0;
    }

    @Override
    public List<Film> getSortedFilms(Long directorId, String sortBy) {
        final String sqlQuery = "SELECT *, COUNT(*) AS likes " +
                "FROM films AS f " +
                "JOIN film_directors AS fd ON f.FILM_ID = fd.FILM_ID " +
                "JOIN mpa AS m ON f.RATING_ID = m.RATING_ID " +
                "LEFT JOIN liked AS l ON f.FILM_ID = l.FILM_ID " +
                "WHERE fd.DIRECTOR_ID = ?" +
                "GROUP BY f.FILM_ID ";

        if (SortBy.YEAR.value.equals(sortBy)) {
            return getFilms(directorId, sqlQuery + "ORDER BY f.RELEASE_DATE");
        }
        if (SortBy.LIKES.value.equals(sortBy)) {
            return getFilms(directorId, sqlQuery + "ORDER BY likes");
        } else {
            throw new RuntimeException("Некорректный параметр запроса \"sortBy\"");
        }
    }

    private List<Film> getFilms(Long directorId, String sqlQuery) {
        return jdbcTemplate.query(sqlQuery, FilmMapper::map, directorId);
    }

    private List<Integer> getIndexesOfSearchedFilms(List<String> search, String queriedText) {
        ArrayList<LinkedHashSet<Integer>> result = new ArrayList<>();
        Arrays.stream(SearchBy.values()).forEach((v) -> result.add(new LinkedHashSet<>()));
        final String sqlQuery = String.format("SELECT id, f_name AS %s, d_name AS %s " +
                "FROM FILMS_VIEW_SEARCH AS f ", SearchBy.TITLE, SearchBy.DIRECTOR);

        jdbcTemplate.query(sqlQuery, (rs) -> {
            for (String s : search) {
                int iSearch = SearchBy.toEnum(s).value;
                String str = rs.getString(iSearch + 1);
                if (str != null && str.trim().toUpperCase().contains(queriedText)) {
                    result.get(iSearch - 1).add(rs.getInt(1));
                }
            }
        });
        LinkedHashSet<Integer> listFilmIndexes = new LinkedHashSet<>();
        for (var s : search) listFilmIndexes.addAll(result.get(SearchBy.toEnum(s).value - 1));

        return listFilmIndexes.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Film> searchFilms(List<String> search, String queriedText) {
        ArrayList<String> searchBy = new ArrayList<>();
        for (String s : search) searchBy.add(s.trim().toUpperCase());
        Collections.reverse(searchBy);
        queriedText = queriedText.trim().toUpperCase();

        List<Integer> listFilmIndexes = getIndexesOfSearchedFilms(searchBy, queriedText);

        ArrayList<Integer> arrayFilmIndexes = new ArrayList<>(listFilmIndexes);
        final String inSql = String.join(",", Collections.nCopies(listFilmIndexes.size(), "?"));
        if(inSql.isEmpty()) return List.of();

        String sqlQuery = String.format("SELECT *, m.MPA " +
                "FROM films AS f " +
                "LEFT JOIN mpa AS m ON f.RATING_ID = m.RATING_ID " +
                "WHERE FILM_ID IN (%s) ", inSql);

        ArrayList<Film> arrayDisorderedFilms = new ArrayList<>(jdbcTemplate.query(sqlQuery, FilmMapper::map, listFilmIndexes.toArray()));
        HashMap<Integer, Film> mapIDtoFile = new HashMap<>();
        IntStream.range(0, arrayDisorderedFilms.size()).forEach(v -> mapIDtoFile.put(Math.toIntExact(arrayDisorderedFilms.get(v).getId()), arrayDisorderedFilms.get(v)));
        ArrayList<Film> arrayOrderedFilms = new ArrayList<>();
        IntStream.range(0, arrayDisorderedFilms.size()).forEach(v -> arrayOrderedFilms.add(mapIDtoFile.get(arrayFilmIndexes.get(v))));
        return arrayOrderedFilms;
    }
}
