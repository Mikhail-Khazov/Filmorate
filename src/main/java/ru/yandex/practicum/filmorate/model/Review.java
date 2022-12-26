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

    private int reviewId;

    @NotBlank(message = "Отзыв не может быть пустым")
    @Size(max = 200, message = "Отзыв должен быть не более 200 символов")
    private String content;

    @NotNull(message = "Необходимо указать поле isPositive")
    @JsonProperty(value = "isPositive")
    private Boolean isPositive;

    @NotNull(message = "Не указан id пользователя")
    private Integer userId;

    @NotNull(message = "Не указан id фильма")
    private Integer filmId;

    private int useful;

}
