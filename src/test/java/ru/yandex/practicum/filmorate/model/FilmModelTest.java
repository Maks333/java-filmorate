package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmModelTest {
    private FilmController controller;


    @BeforeEach
    public void beforeEach() {
        controller = new FilmController();
    }

    //GET tests
    @Test
    public void getEmptyCollectionTest() {
        Collection<Film> films = controller.getFilms();
        assertNotNull(films, "Collection is null");
        assertEquals(0, films.size(), "Size is not 0");
    }

    @Test
    public void getNotEmptyCollectionTest() {
        Film film = new Film();
        film.setName("FilmName");
        film.setDescription("FilmDescription");
        film.setDuration(Duration.ofMinutes(120));
        film.setReleaseDate(LocalDate.of(1999, Month.MARCH, 22));

        Film addedFilm = controller.create(film);
        assertNotNull(addedFilm, "Film is not added");
        assertEquals(1, addedFilm.getId(), "Film has incorrect id");

        Collection<Film> films = controller.getFilms();
        assertEquals(1, films.size(), "Collection contains " + films.size() + " films");
        assertTrue(films.contains(addedFilm), "Film in collection not equal to added film");
    }

    //POST tests
    @Test
    public void createProperlyDefinedFilmTest() {
        Film film = new Film();
        film.setName("FilmName");
        film.setDescription("FilmDescription");
        film.setDuration(Duration.ofMinutes(120));
        film.setReleaseDate(LocalDate.of(1999, Month.MARCH, 22));


        assertDoesNotThrow(() -> controller.create(film), "Validation error occurred");
        List<Film> films = new ArrayList<>(controller.getFilms());
        assertNotNull(films, "Collection is null");
        assertEquals(1, films.size(), "Collection size is not 1");
        assertEquals(1, films.getFirst().getId(), "Added film id is not 1");
    }

    @Test
    public void createNullFilmTest() {
        assertThrows(ValidationException.class, () -> controller.create(null), "Exception is not thrown");
    }

    @Test
    public void createFilmWithInvalidNameTest() {
        Film film = new Film();
        film.setName(null);
        assertThrows(ValidationException.class, () -> controller.create(film), "Exception is not thrown");

        film.setName("     ");
        assertThrows(ValidationException.class, () -> controller.create(film), "Exception is not thrown");

        assertEquals(0, controller.getFilms().size(), "Collection has elements");
    }

    @Test
    public void createFilmWithInvalidDescriptionTest() {}
    @Test
    public void createFilmWithInvalidReleaseDateTest() {}
    @Test
    public void createFilmWithInvalidDurationTest() {}

    //PUT tests
    @Test
    public void updateProperlyDefinedFilmTest() {}
    @Test
    public void updateNullFilmTest() {}
    @Test
    public void updateFilmWithNonExistingIdTest() {}

}
