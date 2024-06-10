package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        log.trace("Enter GET /films endpoint");
        log.debug("Film list : {}", films);
        log.info("Return collection of {} films", films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.trace("Enter POST /films endpoint");
        log.trace("Start film validation for POST /films endpoint");
        validate(film);

        film.setId(getNextId());
        log.debug("Film {} is assigned {} id", film.getName(), film.getId());
        films.put(film.getId(), film);

        log.info("Film: {} with id: {} is added into collection", film.getName(), film.getId());
        return film;
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

    private void validate(Film film) {
        log.trace("Start reference validation");
        if (film == null) {
            log.error("Film object reference to null");
            throw new ValidationException("Film must be not null");
        }

        log.trace("Start name validation");
        log.debug("Name is {}", film.getName());
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Name is empty");
            throw new ValidationException("Name is empty");
        }

        log.trace("Start description validation");
        log.debug("Description is {}", film.getDescription());
        log.debug("Description length is {}", (film.getDescription() == null ? 0 : film.getDescription().length()));
        int MAX_DESCRIPTION_LENGTH = 200;
        if (film.getDescription() == null || film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.error("Film description length is exceed 200 symbols or is empty");
            throw new ValidationException("Film description length exceeds 200 symbols or is empty. Description length: " +
                    (film.getDescription() == null ? 0 : film.getDescription().length()));
        }

        log.trace("Start release date validation");
        log.debug("Release Date is {}", film.getReleaseDate());
        LocalDate LOWER_BOUND_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LOWER_BOUND_RELEASE_DATE)) {
            log.error("Film release date is earlier than 28 december 1895 or null");
            throw new ValidationException("Film release is earlier than 28 december 1895 or is null. Yours is " +
                    film.getReleaseDate());
        }

        log.trace("Start duration validation");
        log.debug("Duration is {}", film.getDuration());
        if (film.getDuration() == null || film.getDuration().isNegative() || film.getDuration().isZero()) {
            log.error("Duration is not a positive number or null");
            throw new ValidationException("Duration is not a positive number or null");
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
