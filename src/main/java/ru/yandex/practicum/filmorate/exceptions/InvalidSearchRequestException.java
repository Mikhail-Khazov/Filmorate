package ru.yandex.practicum.filmorate.exceptions;

public class InvalidSearchRequestException extends RuntimeException {
    public InvalidSearchRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSearchRequestException(String message) {
        this(message, null);
    }

    public InvalidSearchRequestException() {
        this("search operation failed");
    }
}