package ru.yandex.practicum.filmorate.storage.film;

public enum SortBy {
    LIKES("likes"),
    YEAR("year");

    public final String value;

    SortBy(String value) {
        this.value = value;
    }
}
