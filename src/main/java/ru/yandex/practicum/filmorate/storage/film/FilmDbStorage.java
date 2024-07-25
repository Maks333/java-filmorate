package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Repository("dbFilmStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private static final String FIND_BY_ID = "SELECT * FROM films WHERE id = ?";
    private static final String FIND_ALL_FILMS = "SELECT * FROM films";

    private final JdbcTemplate jdbc;
    private final RowMapper<Film> mapper;

    @Override
    public List<Film> getFilms() {
        return jdbc.query(FIND_ALL_FILMS, mapper);
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
    public Film getFilmById(long id) {
        try {
            return jdbc.queryForObject(FIND_BY_ID, mapper, id);
        } catch (EmptyResultDataAccessException ignored) {
            throw new NotFoundException("Film with id: " + id + " is not found");
        }
    }

    @Override
    public void deleteById(long id) {

    }
}
