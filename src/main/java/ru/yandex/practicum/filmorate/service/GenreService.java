package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    public List<FilmGenre> getAll() {
        return genreStorage.getAll();
    }

    public FilmGenre getById(int genreId) {
        FilmGenre genre = genreStorage.getById(genreId).orElseThrow(
                () -> new GenreNotFoundException("Жанр с id: " + genreId + " не найден")
        );
        log.info("Получен жанр c id: {}", genreId);
        return genre;
    }

    public FilmGenre create(FilmGenre genre) {
        return genreStorage.create(genre);
    }

    public void setGenres(List<Film> films) {
        genreStorage.setGenres(films);
    }
}
