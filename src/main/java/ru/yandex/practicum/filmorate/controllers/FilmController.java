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
    public Film get(@PathVariable int filmId) {
        return filmService.get(filmId);
    }

    @GetMapping
    public List<Film> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") @Positive int count) {
        return filmService.getTopFilms(count);
    }

    @GetMapping("/{filmId}/mpa")
    public MPAAFilmRating getRating(@PathVariable int filmId) {
        return filmService.getRating(filmId);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam int userId, @RequestParam int friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @DeleteMapping("/{filmId}")
    public void delete(@PathVariable int filmId) {
        filmService.delete(filmId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getSortedFilms(@PathVariable int directorId, @RequestParam String sortBy) {
        return filmService.getSortedFilms(directorId, sortBy);
    }
}
