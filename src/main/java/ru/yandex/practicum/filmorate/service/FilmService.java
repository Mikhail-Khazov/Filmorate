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
        return genreService.setGenres(List.of(film)).get(0);
    }

    public List<Film> getAll() {
        return genreService.setGenres(filmStorage.getAll());
    }

    public List<Film> getTopFilms(int count) {
        return genreService.setGenres(filmStorage.getTopFilms(count));
    }

    public MPAAFilmRating getRating(int filmId) {
        return filmStorage.getMpaaRating(filmId);
    }

}
