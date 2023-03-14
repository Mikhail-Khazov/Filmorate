package ru.yandex.practicum.filmorate.storage.friends;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;


public interface FriendsStorage {

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    List<User> commonFriends(Long userId, Long friendId);

    List<User> getFriends(Long userId);
}
