package ru.yandex.practicum.filmorate.storage.likes;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Long filmId, Long userId) {
        final String sqlQuery = "INSERT INTO liked (FILM_ID, USER_ID) VALUES (?, ?) ON CONFLICT DO NOTHING ";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        final String sqlQuery = "DELETE FROM liked WHERE FILM_ID = ? AND USER_ID = ?";
        int status = jdbcTemplate.update(sqlQuery, filmId, userId);
        if (status != 1) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public List<Long> getListOfLikes(Long userId) {
        final String sqlQuery = "SELECT FILM_ID FROM liked WHERE USER_ID = ?;";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, userId);
    }
}
