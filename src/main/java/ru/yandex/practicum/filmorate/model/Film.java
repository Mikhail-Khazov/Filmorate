package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validators.After;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class Film {

    private int id;

    @NonNull
    @NotBlank(message = "Название фильма не должно быть пустым")
    private final String name;

    @Size(max = 200, message = "Описание должно быть не более 200 символов")
    private final String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @After(value = "1895-12-28", message = "Некорректная дата релиза")
    private final LocalDate releaseDate;

    @Min(value = 0, message = "Продолжительность не должна быть отрицательной")
    private final long duration;

}
