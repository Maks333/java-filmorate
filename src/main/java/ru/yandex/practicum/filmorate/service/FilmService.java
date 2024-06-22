package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmStorage storage;
    private final UserService userService;

    public List<Film> getFilmsByLikes(long count) {
        return null;
    }

    public void unlikeFilm(long id, long userId) {

    }

    public void likeFilm(long id, long userId) {

    }

    public Film getFilmById(long id) {
        return storage.getFilmById(id);
    }

    public List<Film> getFilms() {
        return storage.getFilms();
    }

    public Film create(Film film) {
        return storage.create(film);
    }

    public Film update(Film film) {
        return storage.update(film);
    }
}
