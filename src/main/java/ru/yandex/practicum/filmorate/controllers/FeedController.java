package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.FeedRow;
import ru.yandex.practicum.filmorate.service.FeedService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{id}/feed")
public class FeedController {

    private final FeedService feedService;

    @GetMapping()
    List<FeedRow> getByUserId(@PathVariable Integer id) {
        return feedService.getByUserId(id);
    }
}