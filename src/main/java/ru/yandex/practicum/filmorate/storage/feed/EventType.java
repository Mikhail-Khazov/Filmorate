package ru.yandex.practicum.filmorate.storage.feed;

public enum EventType {
    FRIEND, LIKE, REVIEW;

    @Override
    public String toString() {
        return this.name();
    }
}