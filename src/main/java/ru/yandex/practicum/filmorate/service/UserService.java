package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User get(int userId) {
        return userStorage.get(userId);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.get(userId);
        User friend = userStorage.get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.get(userId);
        User friend = userStorage.get(friendId);
        Set<Integer> friends = user.getFriends();
        friends.remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getFriends(int userId) {
        User user = userStorage.get(userId);
        return user.getFriends().stream().map(this::get).collect(Collectors.toList());
    }

    public List<User> commonFriends(int userId, int friendId) {
        User user = userStorage.get(userId);
        User friend = userStorage.get(friendId);
        return user.getFriends().stream().filter(u -> friend.getFriends().contains(u)).map(this::get).collect(Collectors.toList());
    }
}
