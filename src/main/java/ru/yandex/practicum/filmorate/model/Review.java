package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;


@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    private int reviewId;

    @NotBlank(message = "Отзыв не может быть пустым")
    @Size(max = 200, message = "Отзыв должен быть не более 200 символов")
    private String content;

    @JsonProperty(value="isPositive")
    @NotNull(message = "Необходимо указать поле isPositive")
    private boolean isPositive;

    @NotNull(message = "Не указан id пользователя")
    private int userId;

    @NotNull(message = "Не указан id фильма")
    private int filmId;

    private int useful;
}
