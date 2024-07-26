package ru.yandex.practicum.filmorate.dbstorages;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureTestDatabase
@JdbcTest
@ComponentScan("ru.yandex.practicum.filmorate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    public void testFindUserById() {
        User user = userStorage.getUserById(1);

        assertThat(user)
                .hasFieldOrPropertyWithValue("id", 1L);
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

        User userFromDb = userStorage.getUserById(6);
        assertThat(userFromDb)
                .hasFieldOrPropertyWithValue("id", 6L);
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

        assertThat(userFromDb.getId()).isEqualTo(5L);
    }

    @Test
    public void testAddFriend() {
        User user = userStorage.getUserById(4);
        System.out.println(user);

        userStorage.addFriend(3, 4);
        User user1 = userStorage.getUserById(3);
        user = userStorage.getUserById(4);

        assertThat(user1.getFriends()).contains(4L);
        assertThat(user.getFriends()).doesNotContain(3L);
    }

    @Test
    public void testFindAllFriends() {
        List<User> friends = userStorage.getFriends(2);

        assertThat(friends.stream().map(User::getId).toList()).contains(3L, 4L);
    }

    @Test
    public void testDeleteFriend() {
        User user = userStorage.getUserById(1);
        assertThat(user.getFriends()).contains(3L);

        userStorage.deleteFriend(1, 3);
        User user1 = userStorage.getUserById(1);
        assertThat(user1.getFriends()).doesNotContain(3L);
    }

    @Test
    public void testCommonFriends() {
        List<User> commonFriends = userStorage.getCommonFriends(3, 4);
        assertThat(commonFriends.getFirst())
                .hasFieldOrPropertyWithValue("id", 2L);
    }
}
