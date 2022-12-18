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

    List<Film> getTopFilms(int count);

    MPAAFilmRating getMpaaRating(int filmId);
}
