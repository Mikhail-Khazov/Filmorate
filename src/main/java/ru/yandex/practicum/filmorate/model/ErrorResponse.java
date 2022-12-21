package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String message;
}
