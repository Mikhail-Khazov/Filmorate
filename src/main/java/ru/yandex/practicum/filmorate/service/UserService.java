package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public User get(int userId) {
        User user = userStorage.get(userId).orElseThrow(
                () -> new UserNotFoundException("Пользователь с id: " + userId + ", не найден")
        );
        log.info("Получен пользователь id: {}", userId);
        return user;
    }

    public User create(User user) {
        nameCheck(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        nameCheck(user);
        return userStorage.update(user).orElseThrow(
                () -> new UserNotFoundException("Пользователь с id: " + user.getId() + ", не найден"));
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }


    private void nameCheck(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("UserService: пользователю с логином \"{}\", в поле \"name\" присвоино значение логина", user.getLogin());
        }
    }

    public void validateUser(int id) {
        userStorage.get(id).orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + id + ", не найден"));
        log.info("Пользователь с id: {}, есть в базе данных", id);
    }
}
