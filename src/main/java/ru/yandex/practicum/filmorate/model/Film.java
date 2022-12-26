package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validators.After;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Film {
    private Long id;

    @NotBlank(message = "Название фильма не должно быть пустым")
    private String name;

    @NotBlank(message = "Описание фильма не может быть пустым")
    @Size(max = 200, message = "Описание должно быть не более 200 символов")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @After(value = "1895-12-28", message = "Некорректная дата релиза")
    private LocalDate releaseDate;

    @Min(value = 0, message = "Продолжительность не должна быть отрицательной")
    private long duration;

    @NotNull
    private MPAAFilmRating mpa;

    private Set<FilmGenre> genres;
    private Set<Director> directors;

    public Film(Long id, String name, String description, LocalDate releaseDate, long duration, MPAAFilmRating mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = new HashSet<>();
        this.directors = new HashSet<>();
    }

    public void addGenre(FilmGenre genre) {
        genres.add(genre);
    }

    public void addDirector(Director director) {
        directors.add(director);
    }
}
