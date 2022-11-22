package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.IdGenerator;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final IdGenerator idGenerator;
    private Map<Integer, User> users = new HashMap<>();

    @Override
    public User get(int userId) {
        if (null != users.get(userId)) {
            log.info("Получен пользователь id: {}", userId);
            return users.get(userId);
        } else throw new UserNotFoundException("Пользователь с id: " + userId + ", не найден");
    }

    @Override
    public User create(User user) {
        nameCheck(user);
        user.setId(idGenerator.generateId());
//        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        log.info("Создание нового пользователя с id: {}", user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        nameCheck(user);
        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
            log.info("Пользователь с id: {} обновлён", user.getId());
            return user;
        } else throw new UserNotFoundException("Невозможно обновить данные пользователя, запись отсутствует");
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    private void nameCheck(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
