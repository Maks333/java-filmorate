package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public void createProperlyDefinedUserTest() {
        User user = new User();
        user.setEmail("email@email.ru");
        user.setLogin("login");
        user.setName("username");
        user.setBirthday(LocalDate.of(1999, Month.MAY, 18));


        assertDoesNotThrow(() -> controller.create(user), "Validation error occurred");
        List<User> users = new ArrayList<>(controller.getUsers());
        assertNotNull(users, "Collection is null");
        assertEquals(1, users.size(), "Collection size is not 1");
        assertEquals(1, users.getFirst().getId(), "Added user id is not 1");
    }

    @Test
    public void createNullUserTest() {
        assertThrows(ValidationException.class, () -> controller.create(null), "Exception is not thrown");
    }

    @Test
    public void createUserWithInvalidEmailTest() {
        User user = new User();
        user.setEmail(null);
        assertThrows(ValidationException.class, () -> controller.create(user), "Exception is not thrown");

        user.setEmail("    ");
        assertThrows(ValidationException.class, () -> controller.create(user), "Exception is not thrown");

        user.setEmail("email");
        assertThrows(ValidationException.class, () -> controller.create(user), "Exception is not thrown");

        assertEquals(0, controller.getUsers().size(), "Collection has elements");
    }

    @Test
    public void createUserWithInvalidLoginTest() {
        User user = new User();
        user.setEmail("email@email.ru");
        user.setLogin(null);
        assertThrows(ValidationException.class, () -> controller.create(user), "Exception is not thrown");

        user.setLogin("      ");
        assertThrows(ValidationException.class, () -> controller.create(user), "Exception is not thrown");

        user.setLogin("user   name");
        assertThrows(ValidationException.class, () -> controller.create(user), "Exception is not thrown");

        assertEquals(0, controller.getUsers().size(), "Collection has elements");
    }

    @Test
    public void createUserWithEmptyNameTest() {
        User user = new User();
        user.setEmail("email@email.ru");
        user.setLogin("login");
        user.setName(null);
        user.setBirthday(LocalDate.of(1999, Month.MAY, 18));
        User createdUser = controller.create(user);

        assertEquals("login", createdUser.getName(), "Username is not equal to login");
        assertEquals(1, createdUser.getId(), "User id is not 1");
        assertEquals(1, controller.getUsers().size(), "Collection size is not 1");

        user = new User();
        user.setEmail("email@email.ru");
        user.setLogin("login12");
        user.setName("            ");
        user.setBirthday(LocalDate.of(1999, Month.MAY, 18));
        createdUser = controller.create(user);
        assertEquals("login12", createdUser.getName(), "Username is not equal to login12");
        assertEquals(2, createdUser.getId(), "User id is not 2");
        assertEquals(2, controller.getUsers().size(), "Collection size is not 2");
    }

    @Test
    public void createUserWithInvalidBirthDayTest() {
        User user = new User();
        user.setEmail("email@email.ru");
        user.setLogin("login");
        user.setName("username");
        user.setBirthday(null);
        assertThrows(ValidationException.class, () -> controller.create(user), "Exception is not thrown");

        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> controller.create(user), "Exception is not thrown");

        assertEquals(0, controller.getUsers().size(), "Collection has elements");
    }

    //PUT tests
    @Test
    public void updateProperlyDefinedUserTest() {
        User user = new User();
        user.setEmail("email@email.ru");
        user.setLogin("login");
        user.setName("username");
        user.setBirthday(LocalDate.of(1999, Month.MAY, 18));
        controller.create(user);

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setEmail("Email@user.ru");
        updatedUser.setLogin("NewLogin");
        updatedUser.setName("UpdatedUsername");
        updatedUser.setBirthday(LocalDate.of(1999, Month.MARCH, 22));
        assertDoesNotThrow(() -> controller.update(updatedUser), "Invalid user");

        List<User> users = new ArrayList<>(controller.getUsers());
        assertEquals(1, users.size(), "Collection size is not 1");
        assertTrue(users.contains(updatedUser), "Collection does not contain updated user");
    }

    @Test
    public void updateNullUserTest() {
        User user = new User();
        user.setEmail("email@email.ru");
        user.setLogin("login");
        user.setName("username");
        user.setBirthday(LocalDate.of(1999, Month.MAY, 18));
        controller.create(user);

        assertThrows(ValidationException.class, () -> controller.update(null), "Exception is not thrown");
    }

    @Test
    public void updateUserWithNonExistingIdTest() {
        User user = new User();
        user.setEmail("email@email.ru");
        user.setLogin("login");
        user.setName("username");
        user.setBirthday(LocalDate.of(1999, Month.MAY, 18));
        controller.create(user);

        User updatedUser = new User();
        updatedUser.setId(15);
        updatedUser.setEmail("Email@user.ru");
        updatedUser.setLogin("NewLogin");
        updatedUser.setName("UpdatedUsername");
        updatedUser.setBirthday(LocalDate.of(1999, Month.MARCH, 22));
        assertThrows(NotFoundException.class,() -> controller.update(updatedUser),
                 "Updated user with non-existing id");
    }
}
