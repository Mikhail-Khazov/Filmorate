package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;

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
        return userStorage.update(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public void addFriend(int userId, int friendId) {
        User user = get(userId);
        User friend = get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = get(userId);
        User friend = get(friendId);
        Set<Integer> friends = user.getFriends();
        friends.remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getFriends(int userId) {
        return userStorage.getFriends(userId);
    }

    public List<User> commonFriends(int userId, int friendId) {
        return userStorage.commonFriends(userId, friendId);
    }

    private void nameCheck(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
