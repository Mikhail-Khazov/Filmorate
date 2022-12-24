package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPAAFilmRating;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film create(Film film);

    int update(Film film);

    Optional<Film> get(int filmId);

    List<Film> getAll();

    List<Film> getTopFilms(int count, Integer genreId, Integer year);

    MPAAFilmRating getMpaaRating(int filmId);

    List<Film> getCommonFilms(int userId, int friendId);

    boolean delete(int filmId);

    List<Film> getSortedFilms(int directorId, String sortBy);

    List<Film> searchFilms(List<String> searchBy, String queriedText);
}
