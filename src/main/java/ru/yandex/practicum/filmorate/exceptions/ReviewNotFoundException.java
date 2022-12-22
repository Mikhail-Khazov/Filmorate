package ru.yandex.practicum.filmorate.exceptions;

public class ReviewNotFoundException extends RuntimeException{
    public ReviewNotFoundException() {
        super();
    }

    public ReviewNotFoundException(String message) {
        super(message);
    }

    public ReviewNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
