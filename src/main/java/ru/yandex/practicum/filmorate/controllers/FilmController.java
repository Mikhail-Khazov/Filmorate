package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    HashMap<Integer, Film> films = new HashMap<>();

    //POST
    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film){
film.setId(new IdGenerator().generateFilmId());
films.put(film.getId(),film);
log.info("Фильм с id: {} добавлен в коллекцию", film.getId());
        return film;
    }

    //PUT
    @PutMapping
    public Film updateFilm (@RequestBody @Valid Film film) {
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            log.info("Фильм с id: {} обновлён", film.getId());
        } else {
            throw new ValidException("Невозможно обновить фильм, запись отсутствует");
//            films.put(film.getId(), film);
//            log.info("Фильм с id: {} добавлен в коллекцию", film.getId());
        }
        return film;
    }

    //GET
//    public  HashMap<Integer, Film> returnFilmsList (){
//        return films;
//    }
@GetMapping
    public List<Film> getAllFilms(){
        return new ArrayList<>(films.values());
    }
}
