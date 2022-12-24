package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikesStorage likesStorage;

    public void addLike(int filmId, int userId) {
        likesStorage.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        likesStorage.deleteLike(filmId, userId);
    }

    public List<Integer> getListOfLikes(int userId) {
        return likesStorage.getListOfLikes(userId);
    }
}
