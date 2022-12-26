package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User create(User user);

    int update(User user);

    Optional<User> get(Long userId);

    List<User> getAll();

    boolean delete(Long userId);
}
