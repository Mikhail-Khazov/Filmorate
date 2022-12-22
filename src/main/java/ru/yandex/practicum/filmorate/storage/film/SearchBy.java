package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exceptions.InvalidSearchRequestException;

public enum SearchBy {
    title(1),
    director(2);

    public final int value;

    SearchBy(int value) {
        this.value = value;
    }

    public static SearchBy toEnum(String s) {
        for (SearchBy e : SearchBy.values())
            if (e.name().equals(s)) return e;
        throw new InvalidSearchRequestException();
    }
}
