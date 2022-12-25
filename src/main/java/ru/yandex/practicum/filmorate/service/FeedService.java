package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FeedRow;
import ru.yandex.practicum.filmorate.storage.feed.FeedDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedService {
    private final FeedDbStorage feedStorage;

    public List<FeedRow> getByUserId(int id) {
        log.info("Запрос на получение ленты новостей пользователя id=" + id);
        return feedStorage.getByUserId(id);
    }
}
