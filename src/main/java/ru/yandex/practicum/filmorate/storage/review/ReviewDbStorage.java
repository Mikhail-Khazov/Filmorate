package ru.yandex.practicum.filmorate.storage.review;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.RowMapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Review> getById(int id) {
        final String sqlQuery = "SELECT r. *, SUM(u.USEFUL) AS USEFULNESS " +
                "FROM reviews AS r " +
                "LEFT JOIN useful AS u ON r.REVIEW_ID = u.REVIEW_ID " +
                "WHERE r.REVIEW_ID = ? " +
                "GROUP BY r.REVIEW_ID ";
        final List<Review> review = jdbcTemplate.query(sqlQuery, RowMapper::mapRowToReview, id);
        return review.stream().findAny();
    }

    @Override
    public Review create(Review review) {
        final String sqlQuery = "INSERT INTO reviews (CONTENT, IS_POSITIVE, USER_ID, FILM_ID) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((connection) -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"REVIEW_ID"});
            statement.setString(1, review.getContent());
            statement.setBoolean(2, review.getIsPositive());
            statement.setInt(3, review.getUserId());
            statement.setInt(4, review.getFilmId());
            return statement;
        }, keyHolder);
        review.setReviewId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return review;
    }

    @Override
    public Optional<Review> update(Review review) {
        final String sqlQuery = "UPDATE reviews " +
                "SET CONTENT = ?, IS_POSITIVE = ?" +
                "WHERE REVIEW_ID = ?";

        jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId()
        );
        return getById(review.getReviewId());
    }

    @Override
    public boolean delete(int id) {
        final String sqlQuery = "DELETE FROM reviews WHERE REVIEW_ID = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public List<Review> getAll(int count) {
        final String sqlQuery = "SELECT r. *, IFNULL(SUM(u.USEFUL), 0) AS USEFULNESS " +
                "FROM reviews AS r " +
                "LEFT JOIN useful AS u ON r.REVIEW_ID = u.REVIEW_ID " +
                "GROUP BY r.REVIEW_ID " +
                "ORDER BY USEFULNESS DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, RowMapper::mapRowToReview, count);
    }

    @Override
    public List<Review> getFilmReviews(int id, int count) {
        final String sqlQuery = "SELECT r. *, IFNULL(SUM(u.USEFUL), 0) AS USEFULNESS " +
                "FROM reviews AS r " +
                "LEFT JOIN useful AS u ON r.REVIEW_ID = u.REVIEW_ID " +
                "WHERE FILM_ID = ? " +
                "GROUP BY r.REVIEW_ID " +
                "ORDER BY USEFULNESS DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, RowMapper::mapRowToReview, id, count);
    }

    @Override
    public void addLike(int id, int userId) {
        final String sqlQuery = "INSERT INTO useful (REVIEW_ID, USER_ID, USEFUL) values (?, ?, 1)";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public void addDislike(int id, int userId) {
        final String sqlQuery = "INSERT INTO useful (REVIEW_ID, USER_ID, USEFUL) values (?, ?, -1)";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public void removeLike(int id, int userId) {
        final String sqlQuery = "DELETE FROM useful WHERE (REVIEW_ID = ?, USER_ID = ?) ";
        jdbcTemplate.update(sqlQuery, id, userId);
    }
}
