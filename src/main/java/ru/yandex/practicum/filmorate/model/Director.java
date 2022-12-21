package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Director {
    private int id;

    @NotBlank(message = "Имя режиссёра не должно быть пустым")
    private String name;
}
