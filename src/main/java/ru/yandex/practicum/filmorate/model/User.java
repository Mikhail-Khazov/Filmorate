package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "email")
@Builder
public class User {

    private int id;

    @NotNull
    @Email(message = "Некорректный email")
    private String email;

    @NotBlank(message = "Login не должен быть пустым")
    @Pattern(regexp = "^\\S*$", message = "Логин не должен содержать пробелы")
    private String login;

    private String name;

    @PastOrPresent(message = "Некорректно указана дата рождения")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}