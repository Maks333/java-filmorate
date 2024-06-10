package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {
    Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validate(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return new Film();
    }

    private void validate(Film film) {
        if (film == null) throw new ValidationException("Film must be not null");

        if (film.getName() == null || film.getName().isBlank()) throw new ValidationException("Name cannot be empty");

        int MAX_DESCRIPTION_LENGTH = 200;
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("Film description length cannot exceed 200 symbols. Description length: " +
                    film.getDescription().length());
        }

        LocalDate LOWER_BOUND_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
        if (film.getReleaseDate().isBefore(LOWER_BOUND_RELEASE_DATE)) {
            throw new ValidationException("Film release date cannot be earlier than 28 december 1895. Yours is " +
                    film.getReleaseDate());
        }

        if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            throw new ValidationException("Duration must be a positive number");
        }
    }

    private int getNextId() {
        int currentMax = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMax;
    }
}
