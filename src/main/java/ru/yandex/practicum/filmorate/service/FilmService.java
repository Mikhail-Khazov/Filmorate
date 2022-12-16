package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPAAFilmRating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validateFilm(film.getId());
        return filmStorage.update(film);
    }

    public Film get(int filmId) {
        Film film = filmStorage.get(filmId).orElseThrow(
                () -> new FilmNotFoundException("Фильм с id: " + filmId + ", не найден")
        );
        log.info("Получен фильм c id: {}", filmId);
        return film;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getTopFilms(count);
    }

    public MPAAFilmRating getRating(int filmId) {
        return filmStorage.getMpaaRating(filmId);
    }

    public void validateFilm(int id) {
        filmStorage.get(id).orElseThrow(() -> new FilmNotFoundException("Фильм с id: " + id + ", не найден"));
        log.info("Фильм с id: {}, есть в базе данных", id);
    }
}
