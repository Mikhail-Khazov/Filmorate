package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {

    Optional<Review> getById(int id);

    Review create(Review review);

    int update(Review review);

    boolean delete(int id);

    List<Review> getAll(int count);

    List<Review> getFilmReviews(int id, int count);

    void addLike(int id, int userId);

    void addDislike(int id, int userId);

    void removeLike(int id, int userId);

    void removeDislike(int id, int userId);
}
