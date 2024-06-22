package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

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
        controller = new FilmController(new FilmService(new InMemoryFilmStorage(),
                new UserService(new InMemoryUserStorage())));
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
    public void createFilmWithInvalidDescriptionTest() {
        Film film = new Film();
        film.setName("FilmName");
        film.setDescription(null);
        assertThrows(ValidationException.class, () -> controller.create(film), "Exception is not thrown");

        film.setDescription("A".repeat(201));
        assertThrows(ValidationException.class, () -> controller.create(film), "Exception is not thrown");

        assertEquals(0, controller.getFilms().size(), "Collection has elements");

    }

    @Test
    public void createFilmWithInvalidReleaseDateTest() {
        Film film = new Film();
        film.setName("FilmName");
        film.setDescription("FilmDescription");
        film.setReleaseDate(null);
        assertThrows(ValidationException.class, () -> controller.create(film), "Exception is not thrown");

        film.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 27));
        assertThrows(ValidationException.class, () -> controller.create(film), "Exception is not thrown");

        assertEquals(0, controller.getFilms().size(), "Collection has elements");
    }

    @Test
    public void createFilmWithInvalidDurationTest() {
        Film film = new Film();
        film.setName("FilmName");
        film.setDescription("FilmDescription");
        film.setReleaseDate(LocalDate.of(1999, Month.MARCH, 22));
        film.setDuration(null);
        assertThrows(ValidationException.class, () -> controller.create(film), "Exception is not thrown");

        film.setDuration(Duration.ofMinutes(0));
        assertThrows(ValidationException.class, () -> controller.create(film), "Exception is not thrown");

        film.setDuration(Duration.ofMinutes(-1));
        assertThrows(ValidationException.class, () -> controller.create(film), "Exception is not thrown");

        assertEquals(0, controller.getFilms().size(), "Collection has elements");
    }

    //PUT tests
    @Test
    public void updateProperlyDefinedFilmTest() {
        Film film = new Film();
        film.setName("FilmName");
        film.setDescription("FilmDescription");
        film.setDuration(Duration.ofMinutes(120));
        film.setReleaseDate(LocalDate.of(1999, Month.MARCH, 22));
        controller.create(film);

        Film updatedFilm = new Film();
        updatedFilm.setId(1);
        updatedFilm.setName("Name");
        updatedFilm.setDescription("Desc");
        updatedFilm.setDuration(Duration.ofMinutes(60));
        updatedFilm.setReleaseDate(LocalDate.of(1999, Month.MARCH, 17));
        assertDoesNotThrow(() -> controller.update(updatedFilm), "Invalid film");

        List<Film> films = new ArrayList<>(controller.getFilms());
        assertEquals(1, films.size(), "Collection size is not 1");
        assertTrue(films.contains(updatedFilm), "Collection does not contain updated film");
    }

    @Test
    public void updateNullFilmTest() {
        Film film = new Film();
        film.setName("FilmName");
        film.setDescription("FilmDescription");
        film.setDuration(Duration.ofMinutes(120));
        film.setReleaseDate(LocalDate.of(1999, Month.MARCH, 22));
        controller.create(film);

        assertThrows(ValidationException.class, () -> controller.update(null), "Exception is not thrown");
    }

    @Test
    public void updateFilmWithNonExistingIdTest() {
        Film film = new Film();
        film.setName("FilmName");
        film.setDescription("FilmDescription");
        film.setDuration(Duration.ofMinutes(120));
        film.setReleaseDate(LocalDate.of(1999, Month.MARCH, 22));
        controller.create(film);

        Film updatedFilm = new Film();
        updatedFilm.setId(15);
        updatedFilm.setName("Name");
        updatedFilm.setDescription("Desc");
        updatedFilm.setDuration(Duration.ofMinutes(60));
        updatedFilm.setReleaseDate(LocalDate.of(1999, Month.MARCH, 17));
        assertThrows(NotFoundException.class, () -> controller.update(updatedFilm), "Exception is not thrown");
    }
}
