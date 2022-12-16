package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.RowMapper;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository("userDBStorage")
@RequiredArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((connection) -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (null == birthday) statement.setNull(4, Types.DATE);
            else statement.setDate(4, Date.valueOf(birthday));
            return statement;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public Optional<User> update(User user) {
        String sqlQuery = "UPDATE users SET EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";

        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return get(user.getId());
    }

    @Override
    public Optional<User> get(int userId) {
        final String sqlQuery = "SELECT USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY FROM users WHERE USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, RowMapper::mapRowToUser, userId);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public List<User> getAll() {
        final String sqlQuery = "SELECT USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY FROM users";
        final List<User> users = jdbcTemplate.query(sqlQuery, RowMapper::mapRowToUser);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Записи с пользователями отсутствуют в базе");
        }
        return users;
    }
}
