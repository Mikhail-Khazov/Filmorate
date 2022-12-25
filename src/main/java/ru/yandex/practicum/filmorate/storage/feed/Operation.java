package ru.yandex.practicum.filmorate.storage.feed;

public enum Operation {
    ADD, REMOVE, UPDATE;

    @Override
    public String toString() {
        return this.name();
    }
}