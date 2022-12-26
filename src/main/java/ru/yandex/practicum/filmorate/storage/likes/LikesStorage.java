package ru.yandex.practicum.filmorate.storage.likes;


import java.util.List;

public interface LikesStorage {

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Long> getListOfLikes(Long userId);
}
