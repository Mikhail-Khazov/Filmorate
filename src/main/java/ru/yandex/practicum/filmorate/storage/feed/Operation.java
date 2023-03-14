package ru.yandex.practicum.filmorate.storage.feed;

import ru.yandex.practicum.filmorate.exceptions.InvalidSearchRequestException;

public enum Operation {
    ADD, REMOVE, UPDATE;

    @Override
    public String toString() {
        return this.name();
    }

    public static Operation toEnum(String s) {
        for (Operation e : Operation.values()) {
            if (e.name().equals(s)) return e;
        }
        throw new InvalidSearchRequestException(" type 'Operation' cast error ");
    }
}