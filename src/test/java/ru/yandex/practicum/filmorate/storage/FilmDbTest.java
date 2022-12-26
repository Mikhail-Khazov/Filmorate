package ru.yandex.practicum.filmorate.storage;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.MPAAFilmRating;
import ru.yandex.practicum.filmorate.model.Film;
import org.junit.jupiter.api.BeforeEach;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmDbTest {

    @Autowired
    private final FilmDbStorage filmDbStorage;
    @Autowired
    private final FilmController filmController;

    @BeforeEach
    public void initialize() {
        filmController.create(new Film(
                1L,
                "Casablanca",
                "Where Love Cuts as Deep as a Dagger!",
                LocalDate.parse("1942-11-26"),
                103,
                new MPAAFilmRating(2L, "PG")));

        filmController.create(new Film(
                2L,
                "Gone with the Wind",
                "The most magnificent picture ever!",
                LocalDate.parse("1939-12-15"),
                222,
                new MPAAFilmRating(2L, "PG")));
    }

    @Test
    void update() {
        filmController.update(
                new Film(1L,
                        "Home Alone",
                        "They Forgot One Minor Detail: Kevin.",
                        LocalDate.parse("1990-12-10"),
                        103L,
                        new MPAAFilmRating(1L, "PG")));

        Optional<Film> filmOptional = filmDbStorage.get(1L);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "Home Alone")
                                .hasFieldOrPropertyWithValue("description", "They Forgot One Minor Detail: Kevin.")
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.parse("1990-12-10"))
                                .hasFieldOrPropertyWithValue("duration", 103L)
                );
    }

    @Test
    void get() {
        Optional<Film> filmOptional = filmDbStorage.get(1L);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "Casablanca")
                                .hasFieldOrPropertyWithValue("description", "Where Love Cuts as Deep as a Dagger!")
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.parse("1942-11-26"))
                                .hasFieldOrPropertyWithValue("duration", 103L)
                );
    }

    @Test
    void getAll() {
        List<Film> films = filmDbStorage.getAll();

        assertThat(films.get(0).getName()).isEqualTo("Casablanca");
        assertThat(films.get(1).getName()).isEqualTo("Gone with the Wind");

    }
}
