package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    List<FilmGenre> getAll();

    Optional<FilmGenre> getById(int id);

    FilmGenre create(FilmGenre genre);

    void setGenres(List<Film> films);
}
