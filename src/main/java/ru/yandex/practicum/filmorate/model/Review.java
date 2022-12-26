package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
public class Review {

    private Long reviewId;

    @NotBlank(message = "Отзыв не может быть пустым")
    @Size(max = 200, message = "Отзыв должен быть не более 200 символов")
    private String content;

    @NotNull(message = "Необходимо указать поле isPositive")
    @JsonProperty(value = "isPositive")
    private Boolean isPositive;

    @NotNull(message = "Не указан id пользователя")
    private Long userId;

    @NotNull(message = "Не указан id фильма")
    private Long filmId;

    private int useful;
}
