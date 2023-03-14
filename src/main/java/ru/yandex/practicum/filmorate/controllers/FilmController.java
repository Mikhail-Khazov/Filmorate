package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPAAFilmRating;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Validated
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        return filmService.update(film);
    }

    @GetMapping("/{filmId}")
    public Film get(@PathVariable Long filmId) {
        return filmService.get(filmId);
    }

    @GetMapping
    public List<Film> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") @Positive int count,
                                  @RequestParam(required = false) Integer genreId,
                                  @RequestParam(required = false) Integer year) {
        return filmService.getTopFilms(count, genreId, year);
    }

    @GetMapping("/{filmId}/mpa")
    public MPAAFilmRating getRating(@PathVariable Long filmId) {
        return filmService.getRating(filmId);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @DeleteMapping("/{filmId}")
    public void delete(@PathVariable Long filmId) {
        filmService.delete(filmId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getSortedFilms(@PathVariable Long directorId, @RequestParam String sortBy) {
        return filmService.getSortedFilms(directorId, sortBy);
    }

    @GetMapping("/search")
    public List<Film> searchFilms(@RequestParam String query, @RequestParam List<String> by) {
        return filmService.searchFilms(by, query);
    }
}
