package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validate(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        validate(user);
        //cannot update user with non-existing id
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("User with " + user.getId() + " is not found");
        }
        users.put(user.getId(), user);
        return user;
    }

    private void validate(User user) {
        //user cannot be null
        if (user == null) {
            throw new ValidationException("User object reference to null");
        }
        //email cannot be null or empty and must contain @ character
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email is empty or does not contain '@' character");
        }
        //login cannot be null or empty or contain space characters
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Login contains space characters or is empty");
        }
        //If name is empty assign login to name field else name
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        //date of birth cannot be in future
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Birthday cannot be in the future");
        }
    }

    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
