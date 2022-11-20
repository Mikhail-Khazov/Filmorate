package ru.yandex.practicum.filmorate.controllersTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = FilmorateApplication.class)
public class UserControllerTest {
    @Autowired
    UserController userController;
    User failureValidationUser;
    User goodValidationUser;
    Validator validator;

    @BeforeEach
    void createContext() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        failureValidationUser = User.builder()
                .email("@mail.su")
                .login("   ")
                .name("Name")
                .birthday(LocalDate.now().plusMonths(1))
                .build();

        goodValidationUser = User.builder()
                .email("test@mail.su")
                .login("Login")
                .name("")
                .birthday(LocalDate.parse("1998-12-26"))
                .build();

        userController.create(failureValidationUser);
        userController.create(goodValidationUser);
    }

    @Test
    void emailMustBeValid() {
        Set<ConstraintViolation<User>> violations = validator.validate(failureValidationUser);
        Set<ConstraintViolation<User>> withoutViolations = validator.validate(goodValidationUser);

        assertTrue(withoutViolations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Некорректный email")));
    }

    @Test
    void loginShouldNotBeBlank() {
        Set<ConstraintViolation<User>> violations = validator.validate(failureValidationUser);
        Set<ConstraintViolation<User>> withoutViolations = validator.validate(goodValidationUser);

        assertTrue(withoutViolations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Login не должен быть пустым")));
    }

    @Test
    void birthdayShouldBeInPast() {
        Set<ConstraintViolation<User>> violations = validator.validate(failureValidationUser);
        Set<ConstraintViolation<User>> withoutViolations = validator.validate(goodValidationUser);

        assertTrue(withoutViolations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Некорректно указана дата рождения")));
    }
}
