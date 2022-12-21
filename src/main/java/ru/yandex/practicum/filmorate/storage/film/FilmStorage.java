package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.MPAAFilmRating;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Optional;
import java.util.List;

public interface FilmStorage {

    Film create(Film film);

    int update(Film film);

    Optional<Film> get(int filmId);

    List<Film> getAll();

    List<Film> getTopFilms(int count);

    MPAAFilmRating getMpaaRating(int filmId);

    List<Film> getCommonFilms (int userId, int friendId);
  
    boolean delete(int filmId);

    List<Film> getSortedFilms(int directorId, String sortBy);
}
