package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserStorage storage;
    private final UserService service;

    @GetMapping
    public List<User> getUsers() {
        return storage.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        return service.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(
            @PathVariable long id,
            @PathVariable long friendId
    ) {
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(
            @PathVariable long id,
            @PathVariable long friendId
    ) {
        service.deleteFried(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        return service.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(
            @PathVariable long id,
            @PathVariable long otherId
    ) {
        return service.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return storage.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return storage.update(user);
    }
}
