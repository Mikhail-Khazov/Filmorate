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

    public Review getById(int id) {
        return reviewStorage.getById(id).orElseThrow(
                () -> new ReviewNotFoundException("Отзыв с id: " + id + ", не найден")
        );
    }

    public Review create(Review review) {
        //TODO
//        userService.get(review.getUserId());
//        filmService.get(review.getFilmId());
        return reviewStorage.create(review);
    }

    public Review update(Review review) {
        if (reviewStorage.update(review) > 0) return review;
        else throw new ReviewNotFoundException("Отзыв с id: " + review.getReviewId() + ", не найден");
    }

    public void delete(int id) {
        if(!reviewStorage.delete(id)){
         throw new ReviewNotFoundException("Отзыв с id: " + id + ", не найден");
        }
    }

    public List<Review> getAll(int id, int count) {
        if (id == 0) return reviewStorage.getAll(count);
        else return reviewStorage.getFilmReviews(id, count);
    }

    public void addLike(int id, int userId) {                   //TODO исключить возможность ставить лайк и дизлайк одновременно
        reviewStorage.addLike(id, userId);
    }

    public void addDislike(int id, int userId) {
        reviewStorage.addDislike(id, userId);
    }

    public void removeLike(int id, int userId) {
        reviewStorage.removeLike(id, userId);                      //TODO Метод должен удалять и лайк и дизлайк
    }

    public void removeDislike(int id, int userId) {
        reviewStorage.removeLike(id, userId);
    }
}
