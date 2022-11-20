package ru.yandex.practicum.filmorate.controllersTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = FilmorateApplication.class)
public class FilmControllerTest {
    @Autowired
    private FilmController filmController;
    private Film failureValidationFilm;
    private Film goodValidationFilm;
    private Validator validator;


    @BeforeEach
    void createContext() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        failureValidationFilm = Film.builder()
                .releaseDate(LocalDate.of(1800, 11, 22))
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                        "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                        "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip")
                .duration(-4560)
                .name("   ")
                .build();

        goodValidationFilm = Film.builder()
                .releaseDate(LocalDate.of(2000, 11, 22))
                .description("Test description")
                .duration(4560)
                .name("Test Name")
                .build();

        filmController.create(failureValidationFilm);
        filmController.create(goodValidationFilm);
    }

    @Test
    void releaseDateTest() {
        Set<ConstraintViolation<Film>> violations = validator.validate(failureValidationFilm);
        Set<ConstraintViolation<Film>> withoutViolations = validator.validate(goodValidationFilm);

        assertTrue(withoutViolations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Некорректная дата релиза")));
    }

    @Test
    void descriptionLengthTest() {
        Set<ConstraintViolation<Film>> violations = validator.validate(failureValidationFilm);
        Set<ConstraintViolation<Film>> withoutViolations = validator.validate(goodValidationFilm);

        assertTrue(withoutViolations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Описание должно быть не более 200 символов")));
    }

    @Test
    void durationShouldNotBeNegative() {
        Set<ConstraintViolation<Film>> violations = validator.validate(failureValidationFilm);
        Set<ConstraintViolation<Film>> withoutViolations = validator.validate(goodValidationFilm);

        assertTrue(withoutViolations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Продолжительность не должна быть отрицательной")));
    }

    @Test
    void nameShouldNotBeBlank() {
        Set<ConstraintViolation<Film>> violations = validator.validate(failureValidationFilm);
        Set<ConstraintViolation<Film>> withoutViolations = validator.validate(goodValidationFilm);

        assertTrue(withoutViolations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Название фильма не должно быть пустым")));
    }
}
