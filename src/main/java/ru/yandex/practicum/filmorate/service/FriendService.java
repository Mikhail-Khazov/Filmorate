package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendService {
    private final FriendsStorage friendsStorage;
    private final UserService userService;

    public void addFriend(int userId, int friendId) {
        userService.validateUser(userId);
        userService.validateUser(friendId);
        friendsStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        userService.validateUser(userId);
        userService.validateUser(friendId);
        friendsStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(int userId) {
        userService.validateUser(userId);
        return friendsStorage.getFriends(userId);
    }

    public List<User> commonFriends(int userId, int friendId) {
        userService.validateUser(userId);
        userService.validateUser(friendId);
        log.info("Получен список общих друзей пользователя id: {}, и пользователя id: {}", userId, friendId);
        List<User> commonFr = friendsStorage.commonFriends(userId, friendId);
        return commonFr;
    }
}
