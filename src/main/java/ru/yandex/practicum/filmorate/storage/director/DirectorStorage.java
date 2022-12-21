package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Optional;
import java.util.List;
import java.util.Set;

public interface DirectorStorage {
    Director create(Director director);

    int update(Director director);

    Optional<Director> get(int directorId);

    List<Director> getAll();

    Set<Director> getDirectorByFilmId(int id);

    boolean delete(int directorId);

    void setDirectors(List<Film> films);
}
