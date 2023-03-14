package ru.yandex.practicum.filmorate.storage.mpaaRating;

import ru.yandex.practicum.filmorate.model.MPAAFilmRating;

import java.util.List;
import java.util.Optional;

public interface MPAARatingStorage {
    List<MPAAFilmRating> getAll();

    Optional<MPAAFilmRating> getById(Long id);
}
