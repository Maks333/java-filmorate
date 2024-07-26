package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;


@Slf4j
@Service
public class FilmService {
    private static final LocalDate LOWER_BOUND_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    private static final int MAX_DESCRIPTION_LENGTH = 200;


    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private MpaDbStorage mpaStorage;
    private GenreDbStorage genreStorage;

    public FilmService(@Qualifier("dbFilmStorage") FilmStorage filmStorage,
                       @Qualifier("dbUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Autowired
    public void setMpaStorage(MpaDbStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Autowired
    public void setGenreStorage(GenreDbStorage genreStorage) {
        this.genreStorage = genreStorage;
    }


    public List<Film> getFilmsByLikes(long count) {
        if (count <= 0) {
            log.error("Parameter count cannot be equal 0 or be negative, yours is {}", count);
            throw new ValidationException("Parameter count cannot be equal 0 or be negative, yours is = " + count);
        }

        return filmStorage.getFilmsByLikes(count);
    }

    public void unlikeFilm(long id, long userId) {
        User user = userStorage.getUserById(userId);
        filmStorage.unlikeFilm(id, userId);
    }

    public void likeFilm(long id, long userId) {
        User user = userStorage.getUserById(userId);
        filmStorage.likeFilm(id, userId);
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film create(Film film) {
        validate(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validate(film);
        return filmStorage.update(film);
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

        if (film.getMpa() != null) {
            try {
                mpaStorage.getMpaById(film.getMpa().getId());
            } catch (NotFoundException ignored) {
                throw new ValidationException("Mpa id is not found");
            }
        }

        if (film.getGenres() != null) {
            try {
                for (Genre genre : film.getGenres()) {
                    genreStorage.getGenreById(genre.getId());
                }
            } catch (NotFoundException ignored) {
                throw new ValidationException("Genre id is not found");
            }
        }
    }
}
