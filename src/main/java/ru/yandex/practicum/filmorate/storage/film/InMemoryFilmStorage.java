package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.controllers.IdGenerator;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
@Getter
public class InMemoryFilmStorage implements FilmStorage{

    private final IdGenerator idGenerator;
    private Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        film.setId(idGenerator.generateId());
        films.put(film.getId(), film);
        log.info("Фильм с id: {} добавлен в коллекцию", film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            log.info("Фильм с id: {} обновлён", film.getId());
        } else {
            throw new FilmNotFoundException("Невозможно обновить фильм, запись отсутствует");
        }
        return film;
    }

    @Override
    public Film get (int filmId){
        if (null != films.get(filmId)) {
            log.info("Получен фильм c id: {}", filmId);
            return films.get(filmId);
        }
        else throw new FilmNotFoundException("Фильм с id: " + filmId + ", не найден");
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }
}
