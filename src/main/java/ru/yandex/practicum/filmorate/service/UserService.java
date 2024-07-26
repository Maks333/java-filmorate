package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
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

//        return user.getFriends().stream()
//                .filter(userId -> otherUser.getFriends().contains(userId))
//                .map(storage::getUserById)
//                .toList();
        return storage.getCommonFriends(id, otherId);
    }

    public List<User> getFriends(long id) {
//        return storage.getUserById(id).getFriends()
//                .stream()
//                .map(storage::getUserById)
//                .toList();
        User user = storage.getUserById(id);

        return storage.getFriends(id);
    }

    public void deleteFriend(long id, long friendId) {
        User user = storage.getUserById(id);
        User friend = storage.getUserById(friendId);
        storage.deleteFriend(id, friendId);
//        user.getFriends().remove(friend.getId());
//        friend.getFriends().remove(user.getId());
//        log.info("User {} and User {} delete each other from friend list", user.getName(), friend.getName());
    }

    public void addFriend(long id, long friendId) {
        User user = storage.getUserById(id);
        User friend = storage.getUserById(friendId);
//        user.getFriends().add(friend.getId());
//        friend.getFriends().add(user.getId());
//        log.info("User {} and User {} add each other into friend list", user.getName(), friend.getName());
        storage.addFriend(id, friendId);
    }

    public User getUser(long id) {
        return storage.getUserById(id);
    }

    public List<User> getUsers() {
        return storage.getUsers();
    }

    public User create(User user) {
        validate(user);
        return storage.create(user);
    }

    public User update(User user) {
        validate(user);
        return storage.update(user);
    }

    private void validate(User user) {
        log.trace("Start reference validation");
        if (user == null) {
            log.error("User object reference to null");
            throw new ValidationException("User object reference to null");
        }

        log.trace("Start email validation");
        log.debug("Email is {}", user.getEmail());
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Email is empty or does not contain '@' character");
            throw new ValidationException("Email is empty or does not contain '@' character");
        }

        log.trace("Start login validation");
        log.debug("Login is {}", user.getLogin());
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Login is empty or contains space characters");
            throw new ValidationException("Login contains space characters or is empty");
        }

        log.trace("Start name validation");
        log.debug("Name is {}", user.getName());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Name is equal to login and its value is {}", user.getName());
        }

        log.trace("Start birthday validation");
        log.debug("Birthday is {}", user.getBirthday());
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Birthday cannot be in the future or be null");
            throw new ValidationException("Birthday cannot be in the future or be null");
        }
    }
}
