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
    public void addLike(int filmId, int userId) {
        //        jdbcTemplate.update("DELETE from liked WHERE FILM_ID=? AND USER_ID=? ", filmId, userId);
        String sqlQuery = "INSERT INTO liked (FILM_ID, USER_ID) VALUES (?, ?) ON CONFLICT DO NOTHING ";
        int status = jdbcTemplate.update(sqlQuery, filmId, userId);
/*
        if (status != 1) {
            throw new UserNotFoundException();
        }

 */
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sqlQuery = "DELETE FROM liked WHERE FILM_ID = ? AND USER_ID = ?";
        int status = jdbcTemplate.update(sqlQuery, filmId, userId);
        if (status != 1) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public List<Integer> getListOfLikes(int userId) {
        String sqlQuery = "SELECT FILM_ID FROM liked WHERE USER_ID = ?;";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, userId);
    }
}
