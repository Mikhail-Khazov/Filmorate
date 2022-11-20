package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;

    @NotNull
    @Email(message = "Некорректный email")
    private final String email;

    @NotBlank(message = "Login не должен быть пустым")
    @Pattern(regexp = "^\\S*$", message = "Логин не должен содержать пробелы")
    private final String login;

    private String name;

    @PastOrPresent(message = "Некорректно указана дата рождения")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate birthday;


}
