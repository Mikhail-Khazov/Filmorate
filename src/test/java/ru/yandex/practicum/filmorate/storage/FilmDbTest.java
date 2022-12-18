package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPAAFilmRating;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    private Film filmSample1;
    private Film filmSample2;

    @BeforeEach
    public void initialize() {
        filmSample1 = Film.builder().name("Casablanca")
                .description("Where Love Cuts as Deep as a Dagger!")
                .releaseDate(LocalDate.parse("1942-11-26"))
                .duration(103)
                .mpa(new MPAAFilmRating(2, "PG"))
                .build();
        filmController.create(filmSample1);

        filmSample2 = Film.builder().name("Gone with the Wind")
                .description("The most magnificent picture ever!")
                .releaseDate(LocalDate.parse("1939-12-15"))
                .duration(222)
                .mpa(new MPAAFilmRating(2, "PG"))
                .build();
        filmController.create(filmSample2);
    }

    @Test
    void update() {
        filmController.update(Film.builder()
                .id(1)
                .name("Home Alone")
                .description("They Forgot One Minor Detail: Kevin.")
                .releaseDate(LocalDate.parse("1990-12-10"))
                .duration(103L)
                .mpa(new MPAAFilmRating(1, "PG"))
                .build());
        Optional<Film> filmOptional = filmDbStorage.get(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("name", "Home Alone")
                                .hasFieldOrPropertyWithValue("description", "They Forgot One Minor Detail: Kevin.")
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.parse("1990-12-10"))
                                .hasFieldOrPropertyWithValue("duration", 103L)
                );
    }

    @Test
    void get() {
        Optional<Film> filmOptional = filmDbStorage.get(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", 1)
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
