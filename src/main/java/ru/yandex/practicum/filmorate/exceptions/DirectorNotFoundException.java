package ru.yandex.practicum.filmorate.exceptions;

public class DirectorNotFoundException extends RuntimeException {
    public DirectorNotFoundException() {
    }

    public DirectorNotFoundException(String message) {
        super(message);
    }

    public DirectorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
