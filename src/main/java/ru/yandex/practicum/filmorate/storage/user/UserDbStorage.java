package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.List;
import java.util.Optional;

@Repository("dbUserStorage")
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private static final String FIND_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String FIND_ALL_USERS = "SELECT * FROM users";
    private static final String INSERT_QUERY = "INSERT INTO users(email, login, name, birthday) " +
            "VALUES(?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String ADD_FRIEND = "INSERT INTO friends(user_id, friend_id) VALUES(?, ?)";

    private static final String FIND_ALL_FRIENDS = "SELECT u.id, email, login, name, birthday FROM friends AS f " +
            " JOIN users AS u ON u.id = f.friend_id " +
            " WHERE user_id = ?";

    private static final String REMOVE_FRIEND = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

    private static final String FIND_COMMON_FRIENDS = "SELECT u.id, u.email, u.login, u.name, u.birthday FROM friends " +
            "JOIN users AS u ON u.id = friend_id " +
            "WHERE user_id = ? AND friend_id IN (SELECT friend_id FROM friends WHERE user_id = ?)";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<User> getUsers() {
        return findMany(FIND_ALL_USERS);
    }

    @Override
    public User create(User user) {
        long id = insert(INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        update(UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return getUserById(user.getId());
    }

    @Override
    public User getUserById(long id) {
        Optional<User> user = findOne(FIND_BY_ID, id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException("User with id = " + id + " is not found");
        }
    }

    @Override
    public void addFriend(long id, long friendId) {
        insert(ADD_FRIEND, id, friendId);
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);

        if (user.getFriends() != null && user.getFriends().contains(friendId)) {
            jdbc.update(REMOVE_FRIEND, id, friendId);
        }
    }

    @Override
    public List<User> getFriends(long id) {
        return findMany(FIND_ALL_FRIENDS, id);
    }

    @Override
    public List<User> getCommonFriends(long id, long otherId) {
        User user = getUserById(id);
        User user1 = getUserById(otherId);
        return findMany(FIND_COMMON_FRIENDS, id, otherId);
    }
}
