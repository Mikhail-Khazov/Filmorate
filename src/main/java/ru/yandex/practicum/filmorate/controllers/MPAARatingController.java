package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPAAFilmRating;
import ru.yandex.practicum.filmorate.service.MPAARatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MPAARatingController {
    private final MPAARatingService mpaaRatingService;

    @GetMapping()
    public List<MPAAFilmRating> getAll() {
        return mpaaRatingService.getAll();
    }

    @GetMapping("/{id}")
    public MPAAFilmRating getById(@PathVariable Long id) {
        return mpaaRatingService.getById(id);
    }
}
