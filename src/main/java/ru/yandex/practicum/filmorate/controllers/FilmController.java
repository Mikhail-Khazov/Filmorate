package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
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
    public Film get (@PathVariable int filmId){
        return filmService.get(filmId);
    }

    @GetMapping
    public List<Film> getAll() {
        return filmService.getAll();
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
filmService.addLike(id,userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
return filmService.getTopFilms(count);
    }
}
