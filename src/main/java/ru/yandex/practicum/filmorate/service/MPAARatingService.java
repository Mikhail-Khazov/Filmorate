package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.MPAARatingNotFoundException;
import ru.yandex.practicum.filmorate.model.MPAAFilmRating;
import ru.yandex.practicum.filmorate.storage.mpaaRating.MPAARatingDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MPAARatingService {
    private final MPAARatingDbStorage mpaaRatingDbStorage;

    public List<MPAAFilmRating> getAll() {
        log.info("Запрос на получение списка возрастных рейтингов");
        return mpaaRatingDbStorage.getAll();
    }

    public MPAAFilmRating getById(int mpaaId) {
        MPAAFilmRating mpa = mpaaRatingDbStorage.getById(mpaaId).orElseThrow(
                () -> new MPAARatingNotFoundException("Возрастного рейтина с id: " + mpaaId + " не сувществует.")
        );
        log.info("Получен возрастной рейтинг c id: {}", mpaaId);
        return mpa;
    }
}
