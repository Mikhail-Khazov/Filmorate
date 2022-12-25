package ru.yandex.practicum.filmorate.storage.likes;


import java.util.List;

public interface LikesStorage {

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Integer> getListOfLikes(int userId);
}
