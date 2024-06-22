package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserStorage storage;

    @GetMapping
    public List<User> getUsers() {
        return storage.getUsers();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return storage.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.trace("Enter PUT /users endpoint");
        log.trace("Start user validation for PUT /users endpoint");
        validate(user);

        log.debug("User id is {}", user.getId());
        if (!users.containsKey(user.getId())) {
            log.error("User {} with {} id is not found", user.getName(), user.getId());
            throw new NotFoundException("User with " + user.getId() + " is not found");
        }
        users.put(user.getId(), user);
        log.info("User with id: {} is updated", user.getId());
        return user;
    }
}
