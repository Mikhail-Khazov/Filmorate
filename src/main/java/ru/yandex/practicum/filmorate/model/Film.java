package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validators.After;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class Film {

    private int id;

    @JsonIgnore
    private Set<Integer> liked = new HashSet<>();

    @NotBlank(message = "Название фильма не должно быть пустым")
    private final String name;

    @NotBlank(message = "Описание фильма не может быть пустым")
    @Size(max = 200, message = "Описание должно быть не более 200 символов")
    private final String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @After(value = "1895-12-28", message = "Некорректная дата релиза")
    private final LocalDate releaseDate;

    @Min(value = 0, message = "Продолжительность не должна быть отрицательной")
    private final long duration;

    @NotNull
    private final MPAAFilmRating rating;

    @NotNull
    private final Set<FilmGenre> genre;

}


