package ru.yandex.practicum.filmorate.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class IdGenerator {
    private int IdCounter;

    public int generateId() {
        return ++IdCounter;
    }
}
