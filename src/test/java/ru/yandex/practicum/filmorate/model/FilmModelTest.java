package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;

public class FilmModelTest {
    private FilmController controller;


    @BeforeEach
    public void beforeEach() {
        controller = new FilmController();
    }

    //GET tests
    @Test
    public void getEmptyCollectionTest() {}
    @Test
    public void getNotEmptyCollectionTest() {}

    //POST tests
    @Test
    public void createProperlyDefinedFilmTest() {}
    @Test
    public void createNullFilmTest() {}
    @Test
    public void createFilmWithInvalidNameTest() {}
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
