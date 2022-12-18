package ru.yandex.practicum.filmorate.exceptions;

public class MPAARatingNotFoundException extends RuntimeException {
    public MPAARatingNotFoundException() {
        super();
    }

    public MPAARatingNotFoundException(String message) {
        super(message);
    }

    public MPAARatingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
