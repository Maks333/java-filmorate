package ru.yandex.practicum.filmorate.dbstorages;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;


import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureTestDatabase
@JdbcTest
@ComponentScan("ru.yandex.practicum.filmorate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    public void testFindUserById() {
        User user = userStorage.getUserById(1);

        assertThat(user)
                .hasFieldOrPropertyWithValue("id", 1L);
        System.out.println(user);
    }

    @Test
    public void testFindAllUsers() {
        List<User> users = userStorage.getUsers();

        assertThat(users).size().isEqualTo(5);
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setEmail("email@gmail.com");
        user.setName("name1");
        user.setLogin("username1");
        user.setBirthday(LocalDate.of(1999, 5, 22));
        User newUser = userStorage.create(user);
        assertThat(newUser)
                .hasFieldOrPropertyWithValue("id", 6L);
        System.out.println(newUser);

        User userFromDb = userStorage.getUserById(6);
        System.out.println(userFromDb);
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setEmail("email@gmail.com");
        user.setName("name1");
        user.setLogin("username1");
        user.setBirthday(LocalDate.of(1999, 5, 22));
        user.setId(5);
        User userFromDb = userStorage.update(user);

        System.out.println(userFromDb);
    }

    @Test
    public void testAddFriend() {
        User user = userStorage.getUserById(4);
        System.out.println(user);

        userStorage.addFriend(3, 4);
        User user1 = userStorage.getUserById(3);
        user = userStorage.getUserById(4);
        System.out.println(user);
        System.out.println(user1);
    }

    @Test
    public void testFindAllFriends() {
        List<User> friends = userStorage.getFriends(2);

        User user = userStorage.getUserById(2);
        System.out.println(user);
        System.out.println(friends);
    }

    @Test
    public void testDeleteFriend() {
        userStorage.deleteFriend(1, 3);

        User user = userStorage.getUserById(1);
        User user1 = userStorage.getUserById(3);
        System.out.println(user);
        System.out.println(user1);
    }

    @Test
    public void testCommonFriends() {
        List<User> commonFriends = userStorage.getCommonFriends(3, 4);
        assertThat(commonFriends.getFirst())
                .hasFieldOrPropertyWithValue("id", 2L);
    }
}
