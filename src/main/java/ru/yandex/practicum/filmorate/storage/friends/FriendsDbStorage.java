package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendsDbStorage implements FriendsStorage, UserMapper {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sqlQuery = "INSERT INTO friends (USER_ID, FRIEND_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        String sqlQuery = "DELETE FROM friends WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> commonFriends(Long userId, Long friendId) {
        String sqlQuery = "SELECT  us.USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY " +
                "FROM users AS us " +
                "JOIN friends AS u1 ON us.USER_ID = u1.FRIEND_ID " +
                "JOIN friends AS u2 ON u1.FRIEND_ID = u2.FRIEND_ID " +
                "WHERE u1.USER_ID = ? AND u2.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, UserMapper::map, userId, friendId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        String sqlQuery = "SELECT u.USER_ID, f.FRIEND_ID, u.EMAIL, u.LOGIN, u.USER_NAME, u.BIRTHDAY " +
                "FROM friends AS f " +
                "LEFT JOIN users AS u ON f.FRIEND_ID = u.USER_ID " +
                "WHERE f.USER_ID = ? ";
        return jdbcTemplate.query(sqlQuery, UserMapper::map, userId);
    }
}
