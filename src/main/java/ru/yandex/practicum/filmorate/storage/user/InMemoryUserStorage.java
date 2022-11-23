package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controllers.IdGenerator;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final IdGenerator idGenerator;
    private Map<Integer, User> users = new HashMap<>();

    @Override
    public Optional<User> get(int userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User create(User user) {
        user.setId(idGenerator.generateId());
        users.put(user.getId(), user);
        log.info("Создание нового пользователя с id: {}", user.getId());
        return user;
    }

    @Override
    public User update(User user) {
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

    @Override
    public List<User> commonFriends(int userId, int friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        return user.getFriends().stream().filter(u -> friend.getFriends().contains(u)).map(u -> users.get(u)).collect(Collectors.toList());
    }

    @Override
    public List<User> getFriends(int userId) {
        User user = users.get(userId);
        return user.getFriends().stream().map(u -> users.get(u)).collect(Collectors.toList());
    }
}
