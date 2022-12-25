package ru.yandex.practicum.filmorate.storage.feed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.time.Instant;


/**
 * class FeedLine
 * - Формирование ленты событий
 * - Записи сохраняются в таблице feed файла schema.sql
 */

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class FeedLine {

    private final JdbcTemplate jdbcTemplate;
    private final FeedDbStorage feedDbStorage;

    private void insertData(int idUser, int idEntity, String event, String operation) {
        final String sqlQuery = "INSERT INTO feed (USER_ID, ENTITY_ID, EVENT_TYPE, OPERATION, TIMELONG) " +
                "VALUES(?, ?, ?, ?, ?) ";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((connection) -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"EVENT_ID"});
            statement.setInt(1, idUser);
            statement.setInt(2, idEntity);
            statement.setString(3, event);
            statement.setString(4, operation);
            statement.setLong(5, Instant.now().toEpochMilli());
            return statement;
        }, keyHolder);
    }

    @AfterReturning("execution(* ru.yandex.practicum.filmorate.service.FriendService.addFriend(..)) && args(id, friendId))")
    public void afterReturningCallAtAddFriend(int id, int friendId) {
        log.info("//////Лента новостей////// Пользователь id_1={} добавил в друзья пользователя id_2={}", id, friendId);
        insertData(id, friendId, EventType.FRIEND.toString(), Operation.ADD.toString());
    }

    @AfterReturning("execution(* ru.yandex.practicum.filmorate.service.FriendService.deleteFriend(..)) && args(id, friendId))")
    public void afterReturningCallAtDeleteFriend(int id, int friendId) {
        log.info("//////Лента новостей////// Пользователь id_1={} удалил пользователя id_2={} из друзей", id, friendId);
        insertData(id, friendId, EventType.FRIEND.toString(), Operation.REMOVE.toString());
    }

    @AfterReturning("execution(* ru.yandex.practicum.filmorate.service.LikeService.addLike(..)) && args(filmId, userId))")
    public void afterReturningCallAtAddLikeFilm(int filmId, int userId) {
        log.info("//////Лента новостей////// Пользователь id_1={} добавил лайк к фильму id_2={}", userId, filmId);
        insertData(userId, filmId, EventType.LIKE.toString(), Operation.ADD.toString());
    }

    @AfterReturning("execution(* ru.yandex.practicum.filmorate.service.LikeService.deleteLike(..)) && args(filmId, userId))")
    public void afterReturningCallAtDeleteLikeFilm(int filmId, int userId) {
        log.info("//////Лента новостей////// Пользователь id_1={} удалил лайк от фильма id_2={}", userId, filmId);
        insertData(userId, filmId, EventType.LIKE.toString(), Operation.REMOVE.toString());
    }

    @AfterReturning("execution(* ru.yandex.practicum.filmorate.service.ReviewService.create(..)) && args(review))")
    public void afterReturningCallAtAddReview(Review review) {
        log.info("//////Лента новостей////// Пользователь id_1={} добавил ревью id_2={}", review.getUserId(), review.getReviewId());
        insertData(review.getUserId(), review.getReviewId(), EventType.REVIEW.toString(), Operation.ADD.toString());
    }

    @AfterReturning("execution(* ru.yandex.practicum.filmorate.service.ReviewService.delete(..)) && args(idReview))")
    public void afterReturningCallAtDeleteReview(int idReview) {
        log.info("//////Лента новостей////// Ревью id_1={} пользователя id_2={} удалено", idReview, feedDbStorage.getIdAuthor(idReview));
        insertData(feedDbStorage.getIdAuthor(idReview), idReview, EventType.REVIEW.toString(), Operation.REMOVE.toString());
    }

    @AfterReturning("execution(* ru.yandex.practicum.filmorate.service.ReviewService.update(..)) && args(review))")
    public void afterReturningCallAtUpdateReview(Review review) {
        log.info("//////Лента новостей////// Ревью id_1={} пользователя id_2={} обновлено", review.getReviewId(), feedDbStorage.getIdAuthor(review.getReviewId()));
        insertData(feedDbStorage.getIdAuthor(review.getReviewId()), review.getReviewId(), EventType.REVIEW.toString(), Operation.UPDATE.toString());
    }
}
