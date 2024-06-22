package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
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
        log.trace("Enter PUT /films endpoint");
        log.trace("Start film validation for PUT /films endpoint");
        validate(film);
        log.debug("Film id is {}", film.getId());
        if (!films.containsKey(film.getId())) {
            log.error("Film {} with {} id is not found", film.getName(), film.getId());
            throw new NotFoundException("Film with id " + film.getId() + " is not found");
        }
        log.info("Film with id: {} is updated", film.getId());
        films.put(film.getId(), film);
        return film;
    }
}
