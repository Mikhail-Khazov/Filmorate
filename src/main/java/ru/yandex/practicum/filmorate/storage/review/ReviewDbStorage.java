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

@Repository
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Review> getById(int id) {
        final String sqlQuery = "SELECT r. *, SUM(u.USEFUL) AS USEFULS " +
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
            statement.setBoolean(2, review.isPositive());
            statement.setInt(3, review.getUserId());
            statement.setInt(4, review.getFilmId());
            return statement;
        }, keyHolder);
        review.setReviewId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return review;
    }

    @Override
    public int update(Review review) {
        final String sqlQuery = "UPDATE reviews " +
                "SET CONTENT = ?, IS_POSITIVE = ?, USER_ID = ?, FILM_ID = ? " +
                "WHERE REVIEW_ID = ?";

        int updatedRowCount = jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.isPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getReviewId()
        );
        return updatedRowCount;
    }

    @Override
    public boolean delete(int id) {
        final String sqlQuery = "DELETE FROM reviews WHERE REVIEW_ID = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public List<Review> getAll(int count) {
        final String sqlQuery = "SELECT r. *, SUM(u.USEFUL) AS USEFULS " +
                "FROM reviews AS r " +
                "LEFT JOIN useful AS u ON r.REVIEW_ID = u.REVIEW_ID " +
                "GROUP BY r.REVIEW_ID " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, RowMapper::mapRowToReview, count);
    }

    @Override
    public List<Review> getFilmReviews(int id, int count) {
        final String sqlQuery = "SELECT r. *, SUM(u.USEFUL) AS USEFULS " +
                "FROM reviews AS r " +
                "LEFT JOIN useful AS u ON r.REVIEW_ID = u.REVIEW_ID " +
                "WHERE FILM_ID = ? " +
                "GROUP BY r.REVIEW_ID " +
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
        final String sqlQuery = "INSERT INTO useful (REVIEW_ID, USER_ID, USEFUL) values (?, ?, 0)";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public void removeDislike(int id, int userId) {                     //TODO delete method
        final String sqlQuery = "INSERT INTO useful (REVIEW_ID, USER_ID, USEFUL) values (?, ?, 0)";
        jdbcTemplate.update(sqlQuery, id, userId);
    }
}
