package ru.yandex.practicum.filmorate.controllers;

import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.model.Director;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @PostMapping
    public Director create(@RequestBody @Valid Director director) {
        return directorService.create(director);
    }

    @PutMapping
    public Director update(@RequestBody @Valid Director director) {
        return directorService.update(director);
    }

    @GetMapping("/{id}")
    public Director get(@PathVariable int id) {
        return directorService.get(id);
    }

    @GetMapping
    public List<Director> getAll() {
        return directorService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        directorService.delete(id);
    }
}
