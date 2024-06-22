package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserStorage storage;

    public List<User> getCommonFriends(long id, long otherId) {
        return List.of();
    }

    public List<User> getFriends(long id) {
        return null;
    }

    public void deleteFried(long id, long friendId) {

    }

    public void addFriend(long id, long friendId) {

    }

    public User getUser(long id) {
        return null;
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
