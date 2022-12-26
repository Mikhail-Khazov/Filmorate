package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {

    Optional<Review> getById(Long id);

    Review create(Review review);

    Optional<Review> update(Review review);

    boolean delete(Long id);

    List<Review> getAll(int count);

    List<Review> getFilmReviews(Long id, int count);

    void addLike(Long id, Long userId);

    void addDislike(Long id, Long userId);

    void removeLike(Long id, Long userId);
}
