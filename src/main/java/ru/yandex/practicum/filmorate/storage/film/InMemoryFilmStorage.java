package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controllers.IdGenerator;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

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
    public Optional<Film> get(int filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public List<Film> getTopFilms(int count) {
        return films.values().stream().sorted((f0, f1) -> {
            return f1.getLiked().size() - f0.getLiked().size();
        }).limit(count).collect(Collectors.toList());
    }
}
