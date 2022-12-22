package ru.yandex.practicum.filmorate.exceptions;

public class ValidException extends RuntimeException {
    public ValidException(String message) {
        super(message);
    }

    public ValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
