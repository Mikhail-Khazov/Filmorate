package ru.yandex.practicum.filmorate.exceptions;

public class InvalidSearchRequestException extends RuntimeException {
    public InvalidSearchRequestException() {
    }

    public InvalidSearchRequestException(String message) {
        super(message);
    }

    public InvalidSearchRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
