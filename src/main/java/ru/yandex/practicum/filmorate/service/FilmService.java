package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPAAFilmRating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreService genreService;

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (filmStorage.update(film) > 0) return film;
        else throw new FilmNotFoundException("Фильм с id: " + film.getId() + ", не найден");
    }

    public Film get(int filmId) {
        Film film = filmStorage.get(filmId).orElseThrow(
                () -> new FilmNotFoundException("Фильм с id: " + filmId + ", не найден")
        );
        log.info("Получен фильм c id: {}", filmId);
        genreService.setGenres(List.of(film));
        return film;
    }

    public List<Film> getAll() {
        List<Film> allFilms = filmStorage.getAll();
        genreService.setGenres(allFilms);
        return allFilms;
    }

    public List<Film> getTopFilms(int count) {
        List<Film> topFilms = filmStorage.getTopFilms(count);
        genreService.setGenres(topFilms);
        return topFilms;
    }

    public MPAAFilmRating getRating(int filmId) {
        return filmStorage.getMpaaRating(filmId);
    }

    public List<Film> getCommonFilms(int userId, int friendId) {
        return filmStorage.getCommonFilms(userId, friendId);
    }
    public void delete(int filmId) {
        if (!filmStorage.delete(filmId)) {
            throw new FilmNotFoundException("Фильм с id: " + filmId + ", не найден");
        }
    }
}
