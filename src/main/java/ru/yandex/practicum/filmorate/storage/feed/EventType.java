package ru.yandex.practicum.filmorate.storage.feed;

import ru.yandex.practicum.filmorate.exceptions.InvalidSearchRequestException;

public enum EventType {
    FRIEND, LIKE, REVIEW;
    public static EventType toEnum(String s) {
        for (EventType e : EventType.values()) {
            if (e.name().equals(s)) return e;
        }
        throw new InvalidSearchRequestException(" type 'EventType' cast error ");
    }
}
