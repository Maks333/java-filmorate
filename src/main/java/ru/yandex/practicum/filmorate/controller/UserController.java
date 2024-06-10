package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        return null;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return null;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        //cannot update user with non-existing id
        return null;
    }

    private void validate(User user) {
        //user cannot be null
        //email cannot be null or empty and must contain @ character
        //login cannot be null or empty or contain space characters
        //If name is empty assign login to name field else name
        //date of birth cannot be in future
    }

    private int getNextId() {
        return 0;
    }
}
