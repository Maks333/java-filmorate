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
        User user = storage.getUserById(id);
        User otherUser = storage.getUserById(otherId);

        return user.getFriends().stream()
                .filter(userId -> otherUser.getFriends().contains(userId))
                .map(userID -> storage.getUsers().get(userID.intValue()))
                .toList();
    }

    public List<User> getFriends(long id) {
        return storage.getUserById(id).getFriends()
                .stream()
                .map(userID -> storage.getUsers().get(userID.intValue()))
                .toList();
    }

    public void deleteFriend(long id, long friendId) {
        User user = storage.getUserById(id);
        User friend = storage.getUserById(friendId);
        user.getFriends().remove(friend.getId());
    }

    public void addFriend(long id, long friendId) {
        User user = storage.getUserById(id);
        User friend = storage.getUserById(friendId);
        user.getFriends().add(friend.getId());
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
