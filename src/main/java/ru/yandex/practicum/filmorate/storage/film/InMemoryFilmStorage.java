package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate LOWER_BOUND_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    @Override
    public List<Film> getFilms() {
        log.trace("Enter GET /films endpoint");
        log.debug("Film list : {}", films);
        log.info("Return collection of {} films", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        log.trace("Enter POST /films endpoint");
        log.trace("Start film validation for POST /films endpoint");
        validate(film);

        film.setId(getNextId());
        log.debug("Film {} is assigned {} id", film.getName(), film.getId());
        films.put(film.getId(), film);

        log.info("Film: {} with id: {} is added into collection", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public void deleteById(int id) {

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
        if (film.getDescription() == null || film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.error("Film description length is exceed 200 symbols or is empty");
            throw new ValidationException("Film description length exceeds 200 symbols or is empty. Description length: " +
                    (film.getDescription() == null ? 0 : film.getDescription().length()));
        }

        log.trace("Start release date validation");
        log.debug("Release Date is {}", film.getReleaseDate());
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
