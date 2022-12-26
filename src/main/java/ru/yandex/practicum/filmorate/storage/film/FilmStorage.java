package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPAAFilmRating;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film create(Film film);

    int update(Film film);

    Optional<Film> get(Long filmId);

    List<Film> getAll();

    List<Film> getTopFilms(int count, Integer genreId, Integer year);

    MPAAFilmRating getMpaaRating(Long filmId);

    List<Film> getCommonFilms(Long userId, Long friendId);

    boolean delete(Long filmId);

    List<Film> getSortedFilms(Long directorId, String sortBy);

    List<Film> searchFilms(List<String> searchBy, String queriedText);
}
