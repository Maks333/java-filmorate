package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService service;

    @GetMapping
    public List<Film> getFilms() {
        return service.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        return service.getFilmById(id);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return service.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return service.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(
            @PathVariable long id,
            @PathVariable long userId
    ) {
        service.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void unlikeFilm(
            @PathVariable long id,
            @PathVariable long userId
    ) {
        service.unlikeFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getFilmsByLikes(@RequestParam(defaultValue = "10") long count) {
        return service.getFilmsByLikes(count);
    }
}
