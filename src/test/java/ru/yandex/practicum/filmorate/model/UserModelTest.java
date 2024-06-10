package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;

import static org.junit.jupiter.api.Assertions.*;

public class UserModelTest {
    private UserController controller;

    @BeforeEach
    public void beforeEach() {
        controller = new UserController();
    }

    //GET tests
    @Test
    public void getEmptyCollectionTest() {}

    @Test
    public void getNotEmptyCollectionTest() {}

    //POST tests
    @Test
    public void createProperlyDefinedUserTest() {}

    @Test
    public void createNullUserTest() {}

    @Test
    public void createUserWithInvalidEmailTest() {}

    @Test
    public void createUserWithInvalidLoginTest() {}

    @Test
    public void createUserWithEmptyNameTest() {}

    @Test
    public void createUserWithInvalidBirthDayTest() {}

    //PUT tests
    @Test
    public void updateProperlyDefinedUserTest() {}

    @Test
    public void updateNullUserTest() {}

    @Test
    public void updateUserWithNonExistingIdTest() {}
}
