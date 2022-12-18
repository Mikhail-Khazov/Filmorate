package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping()
    public List<FilmGenre> getAll() {
        return genreService.getAll();
    }

    @GetMapping("/{id}")
    public FilmGenre getById(@PathVariable int id) {
        return genreService.getById(id);
    }

    @PostMapping
    public FilmGenre create(@RequestBody FilmGenre genre) {
        return genreService.create(genre);
    }
}
