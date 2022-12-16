package ru.yandex.practicum.filmorate.storage.likes;


public interface LikesStorage {

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);
}
