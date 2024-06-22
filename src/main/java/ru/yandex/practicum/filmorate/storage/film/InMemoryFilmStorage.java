package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getFilms() {
        return List.of();
    }

    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public void deleteById(int id) {

    }
}
