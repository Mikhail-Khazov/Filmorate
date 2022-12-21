package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exceptions.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.model.MPAAFilmRating;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final DirectorService directorService;
    private final GenreService genreService;
    private final FilmStorage filmStorage;

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
        directorService.setDirectors(List.of(film));
        return film;
    }

    public List<Film> getAll() {
        List<Film> allFilms = filmStorage.getAll();
        directorService.setDirectors(allFilms);
        genreService.setGenres(allFilms);
        return allFilms;
    }

    public List<Film> getTopFilms(int count) {
        List<Film> topFilms = filmStorage.getTopFilms(count);
        directorService.setDirectors(topFilms);
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

    public List<Film> getSortedFilms(int directorId, String sortBy) {
        List<Film> sortedFilms = filmStorage.getSortedFilms(directorId, sortBy);
        if (sortedFilms.isEmpty()) {
            throw new DirectorNotFoundException("Режиссёр с id: " + directorId + ", не найден");
        }
        directorService.setDirectors(sortedFilms);
        genreService.setGenres(sortedFilms);
        return sortedFilms;
    }
}
