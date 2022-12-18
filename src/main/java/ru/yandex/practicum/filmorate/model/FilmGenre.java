package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class FilmGenre {

    private int id;
    private String name;

    public FilmGenre(String name) {
        this.name = name;
    }
}
