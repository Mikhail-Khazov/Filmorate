package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final UserService userService;
    private final FilmService filmService;

    public Review getById(Long id) {
        return reviewStorage.getById(id).orElseThrow(
                () -> new ReviewNotFoundException("Отзыв с id: " + id + ", не найден")
        );
    }

    public Review create(Review review) {
        validationReview(review);
        return reviewStorage.create(review);
    }

    public Review update(Review review) {
        return reviewStorage.update(review).orElseThrow(
                () -> new ReviewNotFoundException("Отзыв с id: " + review.getReviewId() + ", не найден")
        );
    }

    public void delete(Long id) {
        if (!reviewStorage.delete(id)) {
            throw new ReviewNotFoundException("Отзыв с id: " + id + ", не найден");
        }
    }

    public List<Review> getAll(Long id, int count) {
        if (id == 0) return reviewStorage.getAll(count);
        else return reviewStorage.getFilmReviews(id, count);
    }

    public void addLike(Long id, Long userId) {
        reviewStorage.addLike(id, userId);
    }

    public void addDislike(Long id, Long userId) {
        reviewStorage.addDislike(id, userId);
    }

    public void removeLike(Long id, Long userId) {
        reviewStorage.removeLike(id, userId);
    }

    private void validationReview(Review review) {
        userService.get(review.getUserId());
        filmService.get(review.getFilmId());
    }
}
