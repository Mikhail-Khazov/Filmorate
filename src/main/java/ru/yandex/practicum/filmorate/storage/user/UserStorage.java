package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User create(User user);

    User update(User user);

    Optional<User> get(int userId);

    List<User> getAll();

    List<User> commonFriends(int userId, int friendId);

    List<User> getFriends(int userId);
}
