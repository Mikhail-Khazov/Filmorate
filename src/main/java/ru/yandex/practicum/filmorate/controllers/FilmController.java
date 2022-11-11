package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final IdGenerator idGenerator;

    public FilmController(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    private Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        film.setId(idGenerator.generateId());
        films.put(film.getId(), film);
        log.info("Фильм с id: {} добавлен в коллекцию", film.getId());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            log.info("Фильм с id: {} обновлён", film.getId());
        } else {
            throw new ValidException("Невозможно обновить фильм, запись отсутствует");
        }
        return film;
    }

    @GetMapping
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }
}
