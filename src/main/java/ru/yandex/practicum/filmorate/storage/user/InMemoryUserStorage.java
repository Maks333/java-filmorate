package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        log.trace("Enter GET /users endpoint");
        log.debug("User list: {}", users);
        log.info("Return collection of {} users", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        log.trace("Enter POST /users endpoint");
        log.trace("Start user validation for POST /users endpoint");
        validate(user);

        user.setId(getNextId());
        log.debug("User: {} is assigned {} id", user.getName(), user.getId());
        users.put(user.getId(), user);

        log.info("User: {} with id: {} is added into collection", user.getName(), user.getId());
        return user;
    }

    @Override
    public User update(User user) {
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

    @Override
    public void deleteById(long id) {
        //TODO implement
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

    private long getNextId() {
        long currentMax = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMax;
    }
}
