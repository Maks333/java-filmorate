package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserModelTest {
    private UserController controller;

    @BeforeEach
    public void beforeEach() {
        controller = new UserController();
    }

    //GET tests
    @Test
    public void getEmptyCollectionTest() {
        Collection<User> users = controller.getUsers();
        assertNotNull(users, "Collection is null");
        assertEquals(0, users.size(), "Size is not 0");
    }

    @Test
    public void getNotEmptyCollectionTest() {
        User user = new User();
        user.setEmail("email@email.ru");
        user.setLogin("login");
        user.setName("username");
        user.setBirthday(LocalDate.of(1999, Month.MAY, 18));

        User addedUser = controller.create(user);
        assertNotNull(addedUser, "User is not added");
        assertEquals(1, addedUser.getId(), "User has incorrect id");

        Collection<User> users = controller.getUsers();
        assertEquals(1, users.size(), "Collection contains " + users.size() + " users");
        assertTrue(users.contains(addedUser), "User in collection is not equal to added user");
    }

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
