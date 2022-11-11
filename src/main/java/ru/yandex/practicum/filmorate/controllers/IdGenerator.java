package ru.yandex.practicum.filmorate.controllers;

public class IdGenerator {
    private static int userIdCounter;
    private static int filmIdCounter;

    public int generateUserId() {
        return ++userIdCounter;
    }

    public int generateFilmId() {
        return ++filmIdCounter;
    }
}
