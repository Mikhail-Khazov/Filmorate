package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class FilmGenre {

    private Long id;
    private String name;

    public FilmGenre(String name) {
        this.name = name;
    }
}
