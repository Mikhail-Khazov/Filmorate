package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserDbTest {
    @Autowired
    private final UserDbStorage userDbStorage;
    @Autowired
    private final UserController userController;

    private User johnSample;
    private User janeSample;

    @BeforeEach
    public void initialize() {
        johnSample = User.builder().email("doe@yandex.ru").login("JD").name("John Doe").birthday(LocalDate.parse("1965-01-03")).build();
        userController.create(johnSample);
        janeSample = User.builder().email("jane70@gmail.com").login("MrsJD").name("Jane Doe").birthday(LocalDate.parse("1970-06-23")).build();
        userController.create(janeSample);
    }

    @Test
    void shouldUpdatedUserFromDb() {
        userController.update(User.builder().id(1).email("doe@rambler.ru").login("JCD").name("John Collins Doe").birthday(LocalDate.parse("1983-11-03")).build());
        Optional<User> userOptional = userDbStorage.get(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("email", "doe@rambler.ru")
                                .hasFieldOrPropertyWithValue("login", "JCD")
                                .hasFieldOrPropertyWithValue("name", "John Collins Doe")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.parse("1983-11-03"))
                );
    }

    @Test
    void shouldReturnUserFromDb() {
        Optional<User> userOptional = userDbStorage.get(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("email", "doe@yandex.ru")
                                .hasFieldOrPropertyWithValue("login", "JD")
                                .hasFieldOrPropertyWithValue("name", "John Doe")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.parse("1965-01-03"))
                );
    }

    @Test
    void shouldReturnAllUsersFromDb() {
        List<User> users = userDbStorage.getAll();

        assertThat(users.get(0).getEmail()).isEqualTo("doe@yandex.ru");
        assertThat(users.get(1).getEmail()).isEqualTo("jane70@gmail.com");
    }
}
