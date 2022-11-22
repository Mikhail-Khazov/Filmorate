package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film get(int filmId) {
        return filmStorage.get(filmId);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.get(filmId);
        User user = userStorage.get(userId);
        film.getLiked().add(user.getId());
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.get(filmId);
        User user = userStorage.get(userId);
        film.getLiked().remove(user.getId());
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getAll().stream().sorted((f0, f1) -> {
            return f1.getLiked().size() - f0.getLiked().size();
        }).limit(count).collect(Collectors.toList());
    }


}
