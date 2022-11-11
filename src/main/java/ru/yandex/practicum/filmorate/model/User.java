package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;

    @Email(message = "Некорректный email")
    private final String email;

    @NotBlank(message = "Login не должен быть пустым")
    private final String login;

    private String name;

    @Past(message = "Некорректно указана дата рождения")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate birthday;


}
