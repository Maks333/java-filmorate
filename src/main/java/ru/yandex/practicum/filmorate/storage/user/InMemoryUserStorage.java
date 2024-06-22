package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        return List.of();
    }

    @Override
    public User create(User film) {
        return null;
    }

    @Override
    public User update(User film) {
        return null;
    }

    @Override
    public void deleteById(int id) {

    }
}
