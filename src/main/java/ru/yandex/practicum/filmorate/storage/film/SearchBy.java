package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exceptions.InvalidSearchRequestException;

public enum SearchBy {
    title(1),
    director(2);
    private final int value;

    SearchBy(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.name();
    }

    public int getValue() {
        return value;
    }

    public static SearchBy toEnum(String s) {
        for (SearchBy e : SearchBy.values())
            if (e.name().equals(s)) return e;
        throw new InvalidSearchRequestException();
    }

}
