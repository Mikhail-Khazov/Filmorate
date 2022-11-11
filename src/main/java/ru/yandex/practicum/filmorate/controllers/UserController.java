package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    HashMap<Integer, User> users = new HashMap<>();

    //POST
    @PostMapping
    public User createUser(@Valid @RequestBody User user){
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        user.setId(new IdGenerator().generateUserId());
        users.put(user.getId(),user);
        log.info("Создание нового пользователя с id: {}", user.getId());
        return user;
    }

    //PUT
    @PutMapping
    public User updateUser (@Valid @RequestBody User user) {

        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
            log.info("Пользователь с id: {} обновлён",  user.getId());
        } else {
            throw new ValidException("Невозможно обновить данные пользователя, запись отсутствует");
//            users.put(user.getId(), user);
//            log.info("Создание нового пользователя с id: {}", user.getId());
        }
        return user;
    }

    //GET
//    @GetMapping
//    public  HashMap<Integer, User> returnUsersList (){
//        return users;
//    }

    @GetMapping
    public List<User> getAllUsers(){
        return new ArrayList<>(users.values());
    }
}
