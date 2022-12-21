package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exceptions.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.model.Director;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;

    public Director create(Director director) {
        return directorStorage.create(director);
    }

    public Director update(Director director) {
        if (directorStorage.update(director) > 0) return director;
        else throw new DirectorNotFoundException("Режиссёр с id: " + director.getId() + ", не найден");
    }

    public Director get(int directorId) {
        Director director = directorStorage.get(directorId).orElseThrow(
                () -> new DirectorNotFoundException("Режиссёр с id: " + directorId + ", не найден")
        );
        log.info("Получен режиссёр c id: {}", directorId);
        return director;
    }

    public List<Director> getAll() {
        return directorStorage.getAll();
    }

    public void delete(int directorId) {
        if (!directorStorage.delete(directorId)) {
            throw new DirectorNotFoundException("Режиссёр с id: " + directorId + ", не найден");
        }
    }

    public void setDirectors(List<Film> films) {
        directorStorage.setDirectors(films);
    }
}
