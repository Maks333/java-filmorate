package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FilmorateApplicationTests {
    @Autowired
    FilmController filmController;
    @Autowired
    UserController userController;

    @Test
    void contextLoads() {
        assertNotNull(filmController, "Not loading properly");
        assertNotNull(userController, "Not loading properly");
    }
}
