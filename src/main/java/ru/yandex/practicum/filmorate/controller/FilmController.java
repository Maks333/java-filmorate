package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmStorage storage;

    @GetMapping
    public List<Film> getFilms() {
        return storage.getFilms();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return storage.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return storage.update(film);
    }
}
