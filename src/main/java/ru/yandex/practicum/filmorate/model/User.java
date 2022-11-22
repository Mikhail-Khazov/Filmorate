package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(of = "email")
public class User {

    private int id;

    @JsonIgnore
    private Set<Integer> friends = new HashSet<>();

    @NotNull
    @Email(message = "Некорректный email")
    private final String email;

    @NotBlank(message = "Login не должен быть пустым")
    @Pattern(regexp = "^\\S*$", message = "Логин не должен содержать пробелы")
    private final String login;

    private  String name;

    @PastOrPresent(message = "Некорректно указана дата рождения")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate birthday;


}
