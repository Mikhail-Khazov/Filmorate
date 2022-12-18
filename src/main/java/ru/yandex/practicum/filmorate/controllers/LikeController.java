package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.LikeService;

@RestController
@RequestMapping("/films/{id}/like/{userId}")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PutMapping()
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        likeService.addLike(id, userId);
    }

    @DeleteMapping()
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        likeService.deleteLike(id, userId);
    }
}
