package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exceptions.InvalidSearchRequestException;

public enum SearchBy {
    TITLE(1),
    DIRECTOR(2);

    public final int value;

    SearchBy(int value) {
        this.value = value;
    }
    @Override
    public String toString(){
        return this.name();
    }
    public static SearchBy toEnum(String s) {
        for (SearchBy e : SearchBy.values())
            if (e.name().equals(s)) return e;
        throw new InvalidSearchRequestException();
    }
}
