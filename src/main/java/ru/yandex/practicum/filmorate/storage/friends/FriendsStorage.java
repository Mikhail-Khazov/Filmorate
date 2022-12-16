package ru.yandex.practicum.filmorate.storage.friends;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;


public interface FriendsStorage {

    List<User> addFriend(int userId, int friendId);

    List<User> deleteFriend(int userId, int friendId);

    List<User> commonFriends(int userId, int friendId);

    List<User> getFriends(int userId);
}
