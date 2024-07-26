package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage storage;

    public UserService(@Qualifier("dbUserStorage") UserStorage storage) {
        this.storage = storage;
    }

    public List<User> getCommonFriends(long id, long otherId) {
        User user = storage.getUserById(id);
        User otherUser = storage.getUserById(otherId);

        return user.getFriends().stream()
                .filter(userId -> otherUser.getFriends().contains(userId))
                .map(storage::getUserById)
                .toList();
    }

    public List<User> getFriends(long id) {
        return storage.getUserById(id).getFriends()
                .stream()
                .map(storage::getUserById)
                .toList();
    }

    public void deleteFriend(long id, long friendId) {
        User user = storage.getUserById(id);
        User friend = storage.getUserById(friendId);
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        log.info("User {} and User {} delete each other from friend list", user.getName(), friend.getName());
    }

    public void addFriend(long id, long friendId) {
        User user = storage.getUserById(id);
        User friend = storage.getUserById(friendId);
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
        log.info("User {} and User {} add each other into friend list", user.getName(), friend.getName());
    }

    public User getUser(long id) {
        return storage.getUserById(id);
    }

    public List<User> getUsers() {
        return storage.getUsers();
    }

    public User create(User user) {
        return storage.create(user);
    }

    public User update(User user) {
        return storage.update(user);
    }
}
