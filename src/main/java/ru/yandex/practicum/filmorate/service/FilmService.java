package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(@Qualifier("dbFilmStorage") FilmStorage filmStorage,
                       @Qualifier("dbUserStorage")UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getFilmsByLikes(long count) {
        if (count <= 0) {
            log.error("Parameter count cannot be equal 0 or be negative, yours is {}", count);
            throw new ValidationException("Parameter count cannot be equal 0 or be negative, yours is = " + count);
        }

        return filmStorage.getFilms()
                .stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .toList();
    }

    public void unlikeFilm(long id, long userId) {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(id);
        film.getLikes().remove(user.getId());
        log.info("User {} remove like from {} film", user.getName(), film.getName());
    }

    public void likeFilm(long id, long userId) {
        User user = userStorage.getUserById(userId);
//        Film film = filmStorage.getFilmById(id);
//        film.getLikes().add(user.getId());
//        log.info("User {} add like to {} film", user.getName(), film.getName());
        filmStorage.likeFilm(id, userId);
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }
}
