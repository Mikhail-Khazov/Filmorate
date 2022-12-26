package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;

import java.util.List;

@RestController
@RequestMapping("/users/{id}/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PutMapping("/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        friendService.addFriend(id, friendId);
    }

    @DeleteMapping("/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        friendService.deleteFriend(id, friendId);
    }

    @GetMapping
    public List<User> getFriends(@PathVariable Long id) {
        return friendService.getFriends(id);
    }

    @GetMapping("/common/{otherId}")
    public List<User> commonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return friendService.commonFriends(id, otherId);
    }
}
